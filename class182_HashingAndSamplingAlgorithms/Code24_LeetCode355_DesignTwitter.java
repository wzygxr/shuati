package class107_HashingAndSamplingAlgorithms;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * LeetCode 355. 设计推特 (Design Twitter)
 * 题目链接：https://leetcode.com/problems/design-twitter/
 * 
 * 题目描述：
 * 设计一个简化版的推特(Twitter)，可以让用户发送推文，关注/取消关注其他用户，
 * 能够看见关注人（包括自己）的最近10条推文。
 * 
 * 实现 Twitter 类：
 * - Twitter() 初始化简易版推特对象
 * - void postTweet(int userId, int tweetId) 根据给定的 tweetId 和 userId 创建一条新推文
 * - List<Integer> getNewsFeed(int userId) 检索当前用户新闻推送中最近 10 条推文的 ID
 * - void follow(int followerId, int followeeId) ID 为 followerId 的用户开始关注 ID 为 followeeId 的用户
 * - void unfollow(int followerId, int followeeId) ID 为 followerId 的用户不再关注 ID 为 followeeId 的用户
 * 
 * 算法思路：
 * 1. 使用哈希表存储用户信息和关注关系
 * 2. 使用全局时间戳确保推文按时间排序
 * 3. 使用优先队列(堆)合并多个用户的推文流
 * 4. 限制每个用户的推文数量以节省空间
 * 
 * 时间复杂度：
 * - postTweet: O(1)
 * - getNewsFeed: O(n*log(k))，n为关注用户数，k为每个用户最近推文数
 * - follow: O(1)
 * - unfollow: O(1)
 * 
 * 空间复杂度：O(U + T)，U为用户数，T为推文数
 * 
 * 工程化考量：
 * 1. 线程安全：使用并发数据结构保证多线程环境下的正确性
 * 2. 内存优化：限制每个用户保存的推文数量
 * 3. 性能优化：使用堆合并多个有序推文流
 * 4. 异常处理：处理非法用户ID和操作
 */
public class Code24_LeetCode355_DesignTwitter {
    
    // 推文类
    private static class Tweet {
        int tweetId;
        int userId;
        long timestamp;
        
        Tweet(int tweetId, int userId, long timestamp) {
            this.tweetId = tweetId;
            this.userId = userId;
            this.timestamp = timestamp;
        }
    }
    
    // 用户类
    private static class User {
        int userId;
        Set<Integer> following;  // 关注的用户
        List<Tweet> tweets;      // 发送的推文
        
        User(int userId) {
            this.userId = userId;
            this.following = new HashSet<>();
            this.tweets = new ArrayList<>();
            // 关注自己
            this.following.add(userId);
        }
    }
    
    // 最大保存的推文数量
    private static final int MAX_TWEETS_PER_USER = 100;
    
    // 全局时间戳
    private final AtomicLong timestamp;
    
    // 用户映射
    private final Map<Integer, User> users;
    
    // 线程安全的读写锁
    private final ReadWriteLock lock;
    
    /**
     * 初始化推特系统
     */
    public Code24_LeetCode355_DesignTwitter() {
        this.timestamp = new AtomicLong(0);
        this.users = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }
    
    /**
     * 用户发送推文
     * @param userId 用户ID
     * @param tweetId 推文ID
     */
    public void postTweet(int userId, int tweetId) {
        lock.writeLock().lock();
        try {
            // 获取或创建用户
            User user = users.computeIfAbsent(userId, User::new);
            
            // 创建推文
            Tweet tweet = new Tweet(tweetId, userId, timestamp.incrementAndGet());
            
            // 添加推文到用户
            user.tweets.add(tweet);
            
            // 限制推文数量，移除最旧的推文
            if (user.tweets.size() > MAX_TWEETS_PER_USER) {
                user.tweets.remove(0);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 获取用户新闻推送（最近10条推文）
     * @param userId 用户ID
     * @return 最近10条推文ID列表
     */
    public List<Integer> getNewsFeed(int userId) {
        lock.readLock().lock();
        try {
            // 获取用户
            User user = users.get(userId);
            if (user == null) {
                return new ArrayList<>();
            }
            
            // 使用优先队列合并多个有序推文流
            // 大顶堆，按时间戳排序
            PriorityQueue<Tweet> maxHeap = new PriorityQueue<>((a, b) -> 
                Long.compare(b.timestamp, a.timestamp));
            
            // 将关注用户的最近推文加入堆中
            for (int followeeId : user.following) {
                User followee = users.get(followeeId);
                if (followee != null && !followee.tweets.isEmpty()) {
                    // 只添加最近的推文
                    int start = Math.max(0, followee.tweets.size() - 10);
                    for (int i = start; i < followee.tweets.size(); i++) {
                        maxHeap.offer(followee.tweets.get(i));
                    }
                }
            }
            
            // 获取最近10条推文
            List<Integer> newsFeed = new ArrayList<>();
            int count = 0;
            while (!maxHeap.isEmpty() && count < 10) {
                newsFeed.add(maxHeap.poll().tweetId);
                count++;
            }
            
            return newsFeed;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 用户关注另一个用户
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    public void follow(int followerId, int followeeId) {
        if (followerId == followeeId) {
            // 不能关注自己（除了初始化时）
            return;
        }
        
        lock.writeLock().lock();
        try {
            // 获取或创建关注者
            User follower = users.computeIfAbsent(followerId, User::new);
            
            // 获取或创建被关注者
            users.computeIfAbsent(followeeId, User::new);
            
            // 添加关注关系
            follower.following.add(followeeId);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 用户取消关注另一个用户
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    public void unfollow(int followerId, int followeeId) {
        if (followerId == followeeId) {
            // 不能取消关注自己
            return;
        }
        
        lock.writeLock().lock();
        try {
            User follower = users.get(followerId);
            if (follower != null) {
                follower.following.remove(followeeId);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * 获取系统统计信息
     * @return 统计信息映射
     */
    public Map<String, Object> getStatistics() {
        lock.readLock().lock();
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("userCount", users.size());
            
            int totalTweets = 0;
            int totalFollowing = 0;
            for (User user : users.values()) {
                totalTweets += user.tweets.size();
                totalFollowing += user.following.size();
            }
            
            stats.put("totalTweets", totalTweets);
            stats.put("totalFollowing", totalFollowing);
            stats.put("avgTweetsPerUser", users.isEmpty() ? 0 : (double) totalTweets / users.size());
            stats.put("avgFollowingPerUser", users.isEmpty() ? 0 : (double) totalFollowing / users.size());
            
            return stats;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * 性能测试类
     */
    public static class PerformanceTest {
        
        /**
         * 测试推特系统的性能
         * @param twitter 推特系统实例
         * @param userCount 用户数量
         * @param tweetCount 推文数量
         */
        public static void testTwitterPerformance(Code24_LeetCode355_DesignTwitter twitter, 
                                                  int userCount, int tweetCount) {
            System.out.println("=== 推特系统性能测试 ===");
            System.out.printf("用户数量: %d, 推文数量: %d\n", userCount, tweetCount);
            
            Random random = new Random(42);
            
            // 测试用户操作性能
            long startTime = System.nanoTime();
            
            // 创建用户并发送推文
            for (int i = 0; i < userCount; i++) {
                int userId = i + 1;
                for (int j = 0; j < tweetCount / userCount; j++) {
                    twitter.postTweet(userId, userId * 1000 + j);
                }
            }
            
            long postTime = System.nanoTime() - startTime;
            
            // 测试关注关系
            startTime = System.nanoTime();
            for (int i = 0; i < userCount; i++) {
                int userId = i + 1;
                // 每个用户关注5个随机用户
                for (int j = 0; j < 5; j++) {
                    int followeeId = random.nextInt(userCount) + 1;
                    twitter.follow(userId, followeeId);
                }
            }
            
            long followTime = System.nanoTime() - startTime;
            
            // 测试获取新闻推送
            startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                int userId = random.nextInt(userCount) + 1;
                twitter.getNewsFeed(userId);
            }
            
            long getFeedTime = System.nanoTime() - startTime;
            
            System.out.printf("发送推文平均时间: %.2f ns\n", (double) postTime / tweetCount);
            System.out.printf("建立关注关系平均时间: %.2f ns\n", (double) followTime / (userCount * 5));
            System.out.printf("获取新闻推送平均时间: %.2f ns\n", (double) getFeedTime / 1000);
            
            // 显示统计信息
            System.out.println("系统统计信息: " + twitter.getStatistics());
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 设计推特系统测试 ===\n");
        
        Code24_LeetCode355_DesignTwitter twitter = new Code24_LeetCode355_DesignTwitter();
        
        // 基本功能测试
        System.out.println("1. 基本功能测试:");
        
        // 用户1发送推文
        twitter.postTweet(1, 5);
        System.out.println("用户1发送推文5");
        
        // 用户1获取新闻推送
        List<Integer> feed = twitter.getNewsFeed(1);
        System.out.println("用户1新闻推送: " + feed);
        
        // 用户1关注用户2
        twitter.follow(1, 2);
        System.out.println("用户1关注用户2");
        
        // 用户2发送推文
        twitter.postTweet(2, 6);
        System.out.println("用户2发送推文6");
        
        // 用户1获取新闻推送
        feed = twitter.getNewsFeed(1);
        System.out.println("用户1新闻推送: " + feed);
        
        // 用户1取消关注用户2
        twitter.unfollow(1, 2);
        System.out.println("用户1取消关注用户2");
        
        // 用户1获取新闻推送
        feed = twitter.getNewsFeed(1);
        System.out.println("用户1新闻推送: " + feed);
        
        // 复杂场景测试
        System.out.println("\n2. 复杂场景测试:");
        
        // 创建多个用户和推文
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 5; j++) {
                twitter.postTweet(i, i * 100 + j);
            }
        }
        
        // 建立关注关系
        twitter.follow(1, 2);
        twitter.follow(1, 3);
        
        // 用户1获取新闻推送
        feed = twitter.getNewsFeed(1);
        System.out.println("用户1新闻推送: " + feed);
        
        // 性能测试
        System.out.println("\n3. 性能测试:");
        Code24_LeetCode355_DesignTwitter twitter2 = new Code24_LeetCode355_DesignTwitter();
        PerformanceTest.testTwitterPerformance(twitter2, 100, 1000);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度:");
        System.out.println("- postTweet: O(1)");
        System.out.println("- getNewsFeed: O(n*log(k))，n为关注用户数，k为每个用户最近推文数");
        System.out.println("- follow: O(1)");
        System.out.println("- unfollow: O(1)");
        System.out.println("空间复杂度: O(U + T)，U为用户数，T为推文数");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 社交媒体平台: Twitter、微博等社交平台的消息系统");
        System.out.println("2. 内容推荐系统: 基于用户关注关系的内容推荐");
        System.out.println("3. 新闻聚合系统: 合并多个信息源的新闻内容");
        System.out.println("4. 实时消息系统: 即时通讯应用中的消息推送");
        
        System.out.println("\n=== 设计要点 ===");
        System.out.println("1. 数据结构选择: 哈希表存储用户和关注关系，堆合并推文流");
        System.out.println("2. 内存优化: 限制每个用户保存的推文数量");
        System.out.println("3. 并发安全: 使用读写锁保证多线程环境下的正确性");
        System.out.println("4. 时间排序: 使用全局时间戳确保推文按时间排序");
    }
}