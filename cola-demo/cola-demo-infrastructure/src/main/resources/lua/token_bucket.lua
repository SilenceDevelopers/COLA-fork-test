local tokens_key = KEYS[1]
local max_tokens = tonumber(ARGV[1])
local refill_tokens = tonumber(ARGV[2])
local refill_interval = tonumber(ARGV[3])
local now = tonumber(ARGV[4])

local token_info = redis.call('HMGET', tokens_key, 'tokens', 'last_refill_ts')
local tokens = tonumber(token_info[1])
local last_refill_ts = tonumber(token_info[2])

if tokens == nil then
    tokens = max_tokens
    last_refill_ts = now
end

local delta_ms = now - last_refill_ts
if delta_ms >= refill_interval then
    local refill_count = math.floor(delta_ms / refill_interval) * refill_tokens
    tokens = math.min(tokens + refill_count, max_tokens)
    last_refill_ts = now
end

if tokens > 0 then
    tokens = tokens - 1
    redis.call('HMSET', tokens_key, 'tokens', tokens, 'last_refill_ts', last_refill_ts)
    return 1
else
    redis.call('HMSET', tokens_key, 'tokens', tokens, 'last_refill_ts', last_refill_ts)
    return 0
end
