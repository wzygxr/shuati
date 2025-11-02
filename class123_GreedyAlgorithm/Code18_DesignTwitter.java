package class092;

import java.util.*;

/**
 * LeetCode 355. 设计推特
 * 题目链接：https://leetcode.cn/problems/design-twitter/
 * 难度：中等
 * 
 * 问题描述：
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，以及查看最近 10 条推文。
 * 
 * 实现 Twitter 类：
 * 1. Twitter() 初始化简易版推特对象
 * 2. void postTweet(int userId, int tweetId) 根据给定的 userId 和 tweetId 创建一条新推文。每次调用此函数都会使用一个不同的 tweetId。
 * 3. List<Integer> getNewsFeed(int userId) 检索当前用户及其关注人最近 10 条推文，按时间顺序由近到远排序。
 * 4. void follow(int followerId, int followeeId) ID 为 followerId 的用户开始关注 ID 为 followeeId 的用户。
 * 5. void unfollow(int followerId, int followeeId) ID 为 followerId 的用户不再关注 ID 为 followeeId 的用户。
 * 
 * 解题思路：
 * 贪心算法 + 优先队列（堆）
 * 1. 使用用户ID到其发布推文列表的映射
 * 2. 使用用户ID到其关注列表的映射
 * 3. 对于获取推文功能，使用优先队列按时间顺序合并多个有序列表
 * 4. 使用全局计数器模拟时间戳
 * 
 * 时间复杂度分析：
 * - postTweet: O(1)
 * - follow/unfollow: O(1)
 * - getNewsFeed: O(k log k + 10 log k)，其中k是关注的人数
 * 
 * 空间复杂度分析：
 * - O(n + m)，其中n是用户数，m是推文数
 * 
 * 最优性证明：
 * 使用优先队列来合并多个有序列表是最优的方法，可以保证每次都取出最新的推文，直到获取10条或没有更多推文。
 */
public class Code18_DesignTwitter {
    
    // 推文类，存储推文ID和时间戳
    private class Tweet {
        int tweetId;
        long time;
        Tweet next; // 链表结构，记录用户发布的推文
        
        public Tweet(int tweetId, long time) {
            this.tweetId = tweetId;
            this.time = time;
            this.next = null;
        }
    }
    
    // 存储用户发布的推文，key为用户ID，value为用户的推文链表头
    private Map<Integer, Tweet> userTweets;
    
    // 存储用户的关注关系，key为关注者ID，value为其关注的用户集合
    private Map<Integer, Set<Integer>> userFollows;
    
    // 全局时间戳计数器
    private long timeCounter;
    
    // 每次获取的最大推文数量
    private static final int MAX_TWEETS = 10;
    
    /**
     * 初始化Twitter对象
     */
    public Code18_DesignTwitter() {
        userTweets = new HashMap<>();
        userFollows = new HashMap<>();
        timeCounter = 0;
    }
    
    /**
     * 发布一条新推文
     * @param userId 用户ID
     * @param tweetId 推文ID
     */
    public void postTweet(int userId, int tweetId) {
        // 确保用户存在于数据结构中
        if (!userTweets.containsKey(userId)) {
            userTweets.put(userId, null);
            userFollows.put(userId, new HashSet<>());
        }
        
        // 创建新推文并添加到链表头部（最新的在前面）
        Tweet newTweet = new Tweet(tweetId, timeCounter++);
        newTweet.next = userTweets.get(userId);
        userTweets.put(userId, newTweet);
    }
    
    /**
     * 获取用户及其关注者的最近10条推文
     * @param userId 用户ID
     * @return 按时间倒序排列的推文ID列表
     */
    public List<Integer> getNewsFeed(int userId) {
        List<Integer> result = new ArrayList<>();
        
        // 确保用户存在
        if (!userTweets.containsKey(userId)) {
            userTweets.put(userId, null);
            userFollows.put(userId, new HashSet<>());
        }
        
        // 使用优先队列合并多个有序链表
        // 优先队列按照推文时间降序排列
        PriorityQueue<Tweet> maxHeap = new PriorityQueue<>((a, b) -> Long.compare(b.time, a.time));
        
        // 添加用户自己的最新推文
        if (userTweets.get(userId) != null) {
            maxHeap.offer(userTweets.get(userId));
        }
        
        // 添加用户关注的人的最新推文
        for (int followeeId : userFollows.get(userId)) {
            if (userTweets.containsKey(followeeId) && userTweets.get(followeeId) != null) {
                maxHeap.offer(userTweets.get(followeeId));
            }
        }
        
        // 取出最多10条最新推文
        int count = 0;
        while (!maxHeap.isEmpty() && count < MAX_TWEETS) {
            Tweet current = maxHeap.poll();
            result.add(current.tweetId);
            count++;
            
            // 如果该用户还有更早的推文，将其下一条添加到优先队列
            if (current.next != null) {
                maxHeap.offer(current.next);
            }
        }
        
        return result;
    }
    
    /**
     * 用户关注另一个用户
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    public void follow(int followerId, int followeeId) {
        // 不能关注自己
        if (followerId == followeeId) {
            return;
        }
        
        // 确保用户存在于数据结构中
        if (!userFollows.containsKey(followerId)) {
            userFollows.put(followerId, new HashSet<>());
            userTweets.put(followerId, null);
        }
        if (!userTweets.containsKey(followeeId)) {
            userTweets.put(followeeId, null);
            userFollows.put(followeeId, new HashSet<>());
        }
        
        // 添加关注关系
        userFollows.get(followerId).add(followeeId);
    }
    
    /**
     * 用户取消关注另一个用户
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    public void unfollow(int followerId, int followeeId) {
        // 确保用户存在
        if (!userFollows.containsKey(followerId)) {
            userFollows.put(followerId, new HashSet<>());
            userTweets.put(followerId, null);
        }
        
        // 移除关注关系
        userFollows.get(followerId).remove(followeeId);
    }
    
    /**
     * 测试代码
     */
    public static void main(String[] args) {
        Code18_DesignTwitter twitter = new Code18_DesignTwitter();
        
        // 测试用例1：基本功能测试
        twitter.postTweet(1, 5);  // 用户1发布推文5
        List<Integer> feed1 = twitter.getNewsFeed(1);  // 应该返回 [5]
        System.out.println("测试用例1结果：" + feed1);
        
        // 测试用例2：关注和取消关注
        twitter.follow(1, 2);  // 用户1关注用户2
        twitter.postTweet(2, 6);  // 用户2发布推文6
        List<Integer> feed2 = twitter.getNewsFeed(1);  // 应该返回 [6, 5]
        System.out.println("测试用例2结果：" + feed2);
        
        twitter.unfollow(1, 2);  // 用户1取消关注用户2
        List<Integer> feed3 = twitter.getNewsFeed(1);  // 应该返回 [5]
        System.out.println("测试用例3结果：" + feed3);
        
        // 测试用例3：多个用户发布多条推文
        twitter.postTweet(1, 7);
        twitter.postTweet(1, 8);
        twitter.follow(1, 3);
        twitter.postTweet(3, 9);
        twitter.postTweet(3, 10);
        List<Integer> feed4 = twitter.getNewsFeed(1);  // 应该返回 [10, 9, 8, 7, 5]
        System.out.println("测试用例4结果：" + feed4);
        
        // 测试用例4：边界情况 - 获取超过10条推文
        twitter.postTweet(1, 11);
        twitter.postTweet(1, 12);
        twitter.postTweet(1, 13);
        twitter.postTweet(1, 14);
        twitter.postTweet(1, 15);
        twitter.postTweet(1, 16);
        twitter.postTweet(1, 17);
        List<Integer> feed5 = twitter.getNewsFeed(1);  // 应该只返回最近的10条
        System.out.println("测试用例5结果：" + feed5);
        System.out.println("测试用例5结果长度：" + feed5.size());  // 应该是10
    }
}

/*
工程化考量：
1. 边界条件处理：
   - 处理用户不存在的情况
   - 处理用户不能关注自己的情况
   - 确保获取的推文数量不超过10条

2. 异常处理：
   - 在实际应用中可以添加参数验证
   - 可以定义自定义异常处理特定情况

3. 性能优化：
   - 使用链表存储用户推文，便于快速插入新推文
   - 使用优先队列高效合并多个有序列表
   - 使用HashSet存储关注关系，提高查找效率

4. 代码可读性：
   - 使用内部类Tweet封装推文信息
   - 方法命名清晰，符合Java命名规范
   - 添加详细的注释

5. 扩展性：
   - 可以轻松添加新功能，如点赞、评论等
   - 可以扩展数据存储方式，如使用数据库

6. 并发性：
   - 实际应用中需要考虑线程安全问题
   - 可以使用并发集合或加锁机制

7. 数据结构选择：
   - HashMap: 高效的键值对存储
   - HashSet: 高效的集合操作
   - PriorityQueue: 高效的优先队列操作

8. 调试技巧：
   - 在关键操作处添加日志
   - 使用单元测试验证各个功能
   - 考虑使用断言验证中间结果

9. 与标准库对比：
   - 优先队列的使用符合标准库的最佳实践
   - 集合操作遵循Java集合框架的规范

10. 性能退化排查：
    - 当关注的用户数量很大时，getNewsFeed可能会变慢
    - 可以考虑限制每个用户关注的最大用户数
    - 可以使用缓存机制优化热点用户的推文查询
*/