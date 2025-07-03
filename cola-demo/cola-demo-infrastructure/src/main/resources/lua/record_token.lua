-- record_token.lua
local bloomKey = KEYS[1]
local setKey = KEYS[2]
local token = ARGV[1]

redis.call('BF.ADD', bloomKey, token)
redis.call('SADD', setKey, token)
return 1
