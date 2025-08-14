`Architecture at Level 1`

Single instance of Spring Boot app.

Single PostgreSQL instance (local or Docker).

No caching layer.

No load balancer.

Minimal validation & error handling.



1️⃣ When we start hitting high write volume
What happens:

Every new URL needs an INSERT into Postgres to get its id.

If we have very high QPS (e.g., thousands of new shortens/sec), DB writes can become a bottleneck.

This isn’t a Base62 problem per se — it’s the fact that Base62 depends on sequential DB IDs.

Possible fixes (later stage):

Pre-allocate ID ranges to each app instance.

Move to an ID generator service (e.g., Snowflake IDs, Twitter Snowflake, UUID-based but shorter).

Batch inserts or async processing.

2️⃣ When we deploy multiple instances
What happens:

With a single DB, auto-increment still works fine across instances.

But if we ever split into multiple DBs (e.g., sharding), IDs will overlap unless we coordinate.

Base62 encoding assumes unique numeric IDs across the system.

Possible fixes:

Use a central ID service (Redis INCR, Snowflake, etc.).

Encode shard ID into the short code.

3️⃣ When URL shortening must be idempotent
What happens:

Our current flow always creates a new row → new ID → new short code.

If the same long URL is shortened 1M times, we store 1M rows — wasted space.

At low load this is fine; at high scale, it’s wasteful.

Possible fixes:

Maintain a hash (e.g., SHA-256) of the long URL and check before inserting.

Trade-off: Extra DB lookup before insert adds latency.

4️⃣ When you need stronger security or unpredictability
What happens:

Base62 IDs are predictable — short code abc will be followed by abd.

At small scale, not an issue. At larger scale, it allows scraping of all short URLs.

If you store private URLs, this is a risk.

Possible fixes:

Use random Base62 strings instead of sequential IDs.

Trade-off: Need collision detection on insert.

