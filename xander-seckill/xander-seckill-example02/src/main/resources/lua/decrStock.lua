-- 从参数中获取Key值
local key = KEYS[1]
-- 获取剩余库存值并转为数字
local remainStock = tonumber(redis.call('get',key))
-- Redis里面没有缓存库存值
if(remainStock == nil) then
    return nil
end
-- 如果剩余库存不为空，并且大于0.就可以扣除
if remainStock ~= nil and remainStock > 0 then
    local current = tonumber(redis.call('decr', key))
    return current
end
-- 如果剩余库存已经为0，直接返回-1
if remainStock ~= nil and remainStock == 0 then
    return -1
end