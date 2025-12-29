// supabase/functions/insert-post/index.ts
import { createClient } from 'https://esm.sh/@supabase/supabase-js@2'
import { serve } from 'https://deno.land/std@0.168.0/http/server.ts'

serve(async (req) => {
  try {
    if (req.method !== 'POST') {
      return new Response(JSON.stringify({ error: 'Method Not Allowed' }), {
        status: 405,
        headers: { 'Content-Type': 'application/json' },
      })
    }

    // 1. Get the user's authentication token from the request header
    const authHeader = req.headers.get('Authorization')
    if (!authHeader) {
      return new Response(JSON.stringify({ error: 'Unauthorized: No Authorization header' }), {
        status: 401,
        headers: { 'Content-Type': 'application/json' },
      })
    }

    // Extract the JWT token
    const jwt = authHeader.split('Bearer ')[1]
    if (!jwt) {
      return new Response(JSON.stringify({ error: 'Unauthorized: Invalid Authorization header' }), {
        status: 401,
        headers: { 'Content-Type': 'application/json' },
      })
    }

    // 2. Create a Supabase client to verify the user's JWT
    // We use the ANON_KEY here, as the user's client-side JWT is tied to the anon role.
    const supabaseAnon = createClient(
      Deno.env.get('SUPABASE_URL')!,
      Deno.env.get('SUPABASE_ANON_KEY')!,
      { global: { headers: { Authorization: authHeader } } }
    )

    // Verify the user's session and get the user object
    const { data: { user }, error: authError } = await supabaseAnon.auth.getUser()

    if (authError || !user) {
      console.error("Authentication error:", authError?.message || "User not found")
      return new Response(JSON.stringify({ error: 'Unauthorized: Invalid token or user not found' }), {
        status: 401,
        headers: { 'Content-Type': 'application/json' },
      })
    }

    // At this point, `user` is confirmed to be an authenticated user.
    // You can add more complex authorization logic here if needed (e.g., check user roles)
    console.log("Authenticated user:", user.id);

    // 3. Parse the request body to get the 'content' for the post
    const { content } = await req.json()
    if (!content || typeof content !== 'string' || content.trim() === '') {
      return new Response(JSON.stringify({ error: 'Invalid content provided' }), {
        status: 400,
        headers: { 'Content-Type': 'application/json' },
      })
    }

    // 4. Create a privileged Supabase client using the SERVICE_ROLE_KEY
    // This client bypasses RLS and can write to any table.
    const supabaseAdmin = createClient(
      Deno.env.get('SUPABASE_URL')!,
      Deno.env.get('SUPABASE_SERVICE_ROLE_KEY')!
    )

    // 5. Insert the post into the 'posts' table
    const { data, error } = await supabaseAdmin
      .from('posts')
      .insert({ content: content, user_id: user.id }) // Map to your table columns
      .select() // To return the inserted data

    if (error) {
      console.error("Database insert error:", error.message);
      return new Response(JSON.stringify({ error: 'Failed to insert post', details: error.message }), {
        status: 500,
        headers: { 'Content-Type': 'application/json' },
      })
    }

    return new Response(JSON.stringify({ message: 'Post created successfully', data: data }), {
      status: 201,
      headers: { 'Content-Type': 'application/json' },
    })

  } catch (err) {
    console.error("Edge Function error:", err);
    return new Response(JSON.stringify({ error: `Internal Server Error: ${err.message}` }), {
      status: 500,
      headers: { 'Content-Type': 'application/json' },
    })
  }
})