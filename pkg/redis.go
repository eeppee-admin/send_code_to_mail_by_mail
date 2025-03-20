package pkg

import (
	"context"
	"time"

	"github.com/go-redis/redis/v8"
)

var ctx = context.Background()

// RedisClient 定义一个RedisClient结构体
type RedisClient struct {
	client *redis.Client
}

// NewRedisClient 创建一个RedisClient实例
func NewRedisClient() *RedisClient {
	rdb := redis.NewClient(&redis.Options{
		Addr:     "localhost:6379",
		Password: "", // no password set
		DB:       0,  // use default DB
	})
	return &RedisClient{client: rdb}
}

// SetCode 设置一个验证码
func (r *RedisClient) SetCode(email string, code string, ttl time.Duration) error {
	return r.client.Set(ctx, email, code, ttl).Err()
}

// GetCode 获取一个验证码
func (r *RedisClient) GetCode(email string) (string, error) {
	return r.client.Get(ctx, email).Result()
}
