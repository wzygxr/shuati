package class027;

import java.util.*;

/**
 * LeetCode 355: 设计推特
 * <p>
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，
 * 以及能够看见关注人（包括自己）的最近10条推文。
 * <p>
 * 解题思路：
 * 1. 使用字典存储用户信息、关注列表和推文
 * 2. 使用优先队列（堆）来按时间线合并推文
 * 3. 使用一个全局计数器模拟时间戳
 */
public class Code17_Twitter {

    /**
     * Twitter类，实现了用户发布推文、关注/取消关注用户、获取最新动态等功能
     * 使用堆来高效获取最新动态
     */
    public static class Twitter {
        // 用户ID到其关注的用户ID集合的映射
        private Map<Integer, Set<Integer>> follows;
        // 用户ID到其发布的推文列表的映射
        private Map<Integer, List<Tweet>> tweets;
        // 全局时间戳，用于记录推文的发布顺序
        private int globalTime;

        /**
         * 推文内部类，存储推文ID和发布时间
         */
        private static class Tweet {
            int id;      // 推文ID
            int time;    // 发布时间

            public Tweet(int id, int time) {
                this.id = id;
                this.time = time;
            }
        }

        /**
         * 初始化Twitter对象
         */
        public Twitter() {
            this.follows = new HashMap<>();
            this.tweets = new HashMap<>();
            this.globalTime = 0;
        }

        /**
         * 用户发布一条新推文
         *
         * @param userId  用户ID
         * @param tweetId 推文ID
         */
        public void postTweet(int userId, int tweetId) {
            // 确保用户存在
            if (!follows.containsKey(userId)) {
                follows.put(userId, new HashSet<>());
                follows.get(userId).add(userId); // 默认关注自己
                tweets.put(userId, new ArrayList<>());
            }

            // 发布推文，使用负时间戳以便在优先队列中按时间倒序排列
            tweets.get(userId).add(new Tweet(tweetId, globalTime));
            globalTime++;
        }

        /**
         * 获取用户关注的所有人（包括自己）发布的最近10条推文
         *
         * @param userId 用户ID
         * @return 按发布时间倒序排列的最近10条推文ID列表
         */
        public List<Integer> getNewsFeed(int userId) {
            // 确保用户存在
            if (!follows.containsKey(userId)) {
                follows.put(userId, new HashSet<>());
                follows.get(userId).add(userId);
                tweets.put(userId, new ArrayList<>());
                return new ArrayList<>();
            }

            // 使用最大堆来获取最新的推文
            // 堆中每个元素是: (-时间戳, 推文ID, 用户ID, 该用户下一条推文的索引)
            // 注意：使用负数时间戳，因为Java的PriorityQueue默认是最小堆
            PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
                    (a, b) -> Integer.compare(b[0], a[0])
            );

            // 初始化堆，为每个关注的用户添加最新的推文
            for (int followeeId : follows.get(userId)) {
                List<Tweet> userTweets = tweets.get(followeeId);
                if (userTweets != null && !userTweets.isEmpty()) {
                    // 获取该用户最新的推文（最后发布的）
                    int lastIdx = userTweets.size() - 1;
                    Tweet lastTweet = userTweets.get(lastIdx);
                    maxHeap.offer(new int[]{
                            lastTweet.time,
                            lastTweet.id,
                            followeeId,
                            lastIdx - 1
                    });
                }
            }

            // 获取最近的10条推文
            List<Integer> result = new ArrayList<>();
            while (!maxHeap.isEmpty() && result.size() < 10) {
                int[] top = maxHeap.poll();
                result.add(top[1]);

                // 如果该用户还有更早的推文，添加到堆中
                if (top[3] >= 0) {
                    List<Tweet> userTweets = tweets.get(top[2]);
                    Tweet nextTweet = userTweets.get(top[3]);
                    maxHeap.offer(new int[]{
                            nextTweet.time,
                            nextTweet.id,
                            top[2],
                            top[3] - 1
                    });
                }
            }

            return result;
        }

        /**
         * 用户关注另一个用户
         *
         * @param followerId 关注者ID
         * @param followeeId 被关注者ID
         */
        public void follow(int followerId, int followeeId) {
            // 确保两个用户都存在
            if (!follows.containsKey(followerId)) {
                follows.put(followerId, new HashSet<>());
                follows.get(followerId).add(followerId); // 默认关注自己
                tweets.put(followerId, new ArrayList<>());
            }

            if (!follows.containsKey(followeeId)) {
                follows.put(followeeId, new HashSet<>());
                follows.get(followeeId).add(followeeId); // 默认关注自己
                tweets.put(followeeId, new ArrayList<>());
            }

            // 添加关注关系
            follows.get(followerId).add(followeeId);
        }

        /**
         * 用户取消关注另一个用户
         *
         * @param followerId 关注者ID
         * @param followeeId 被关注者ID
         */
        public void unfollow(int followerId, int followeeId) {
            // 确保关注者存在
            if (!follows.containsKey(followerId)) {
                follows.put(followerId, new HashSet<>());
                follows.get(followerId).add(followerId); // 默认关注自己
                tweets.put(followerId, new ArrayList<>());
                return;
            }

            // 确保被关注者存在
            if (!follows.containsKey(followeeId)) {
                follows.put(followeeId, new HashSet<>());
                follows.get(followeeId).add(followeeId); // 默认关注自己
                tweets.put(followeeId, new ArrayList<>());
                return;
            }

            // 不能取消关注自己
            if (followerId != followeeId) {
                follows.get(followerId).remove(followeeId);
            }
        }
    }

    /**
     * Twitter类的另一种实现，使用更简单的数据结构
     * 适用于了解基本功能实现
     */
    public static class AlternativeTwitter {
        // 存储所有推文 (时间戳, userID, tweetID)
        private List<int[]> allTweets;
        // 存储用户关注关系
        private Map<Integer, Set<Integer>> follows;
        private int time;

        public AlternativeTwitter() {
            this.allTweets = new ArrayList<>();
            this.follows = new HashMap<>();
            this.time = 0;
        }

        public void postTweet(int userId, int tweetId) {
            // 确保用户存在
            if (!follows.containsKey(userId)) {
                follows.put(userId, new HashSet<>());
                follows.get(userId).add(userId); // 默认关注自己
            }

            // 添加推文
            allTweets.add(new int[]{time++, userId, tweetId});
        }

        public List<Integer> getNewsFeed(int userId) {
            // 确保用户存在
            if (!follows.containsKey(userId)) {
                follows.put(userId, new HashSet<>());
                follows.get(userId).add(userId); // 默认关注自己
                return new ArrayList<>();
            }

            // 筛选出关注的用户的推文
            List<Integer> result = new ArrayList<>();
            // 从最近的推文开始遍历
            for (int i = allTweets.size() - 1; i >= 0 && result.size() < 10; i--) {
                int[] tweet = allTweets.get(i);
                if (follows.get(userId).contains(tweet[1])) {
                    result.add(tweet[2]);
                }
            }

            return result;
        }

        public void follow(int followerId, int followeeId) {
            // 确保两个用户都存在
            if (!follows.containsKey(followerId)) {
                follows.put(followerId, new HashSet<>());
                follows.get(followerId).add(followerId); // 默认关注自己
            }

            if (!follows.containsKey(followeeId)) {
                follows.put(followeeId, new HashSet<>());
                follows.get(followeeId).add(followeeId); // 默认关注自己
            }

            // 添加关注关系
            follows.get(followerId).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            // 确保关注者存在
            if (!follows.containsKey(followerId)) {
                follows.put(followerId, new HashSet<>());
                follows.get(followerId).add(followerId); // 默认关注自己
                return;
            }

            // 不能取消关注自己
            if (followerId != followeeId) {
                follows.get(followerId).remove(followeeId);
            }
        }
    }

    /**
     * 优化版的Twitter实现，针对大规模数据优化了性能
     * 使用更高效的数据结构和算法
     */
    public static class OptimizedTwitter {
        // 用户ID -> 最近的推文列表，限制存储最近10条
        private Map<Integer, List<Tweet>> userTweets;
        // 用户ID -> 关注的用户ID集合
        private Map<Integer, Set<Integer>> userFollows;
        private int time;

        private static class Tweet {
            int id;
            int time;

            public Tweet(int id, int time) {
                this.id = id;
                this.time = time;
            }
        }

        public OptimizedTwitter() {
            this.userTweets = new HashMap<>();
            this.userFollows = new HashMap<>();
            this.time = 0;
        }

        public void postTweet(int userId, int tweetId) {
            // 确保用户存在
            if (!userFollows.containsKey(userId)) {
                userFollows.put(userId, new HashSet<>());
                userFollows.get(userId).add(userId); // 默认关注自己
                userTweets.put(userId, new ArrayList<>());
            }

            // 添加推文，只保留最近的10条
            List<Tweet> tweets = userTweets.get(userId);
            tweets.add(new Tweet(tweetId, time++));
            if (tweets.size() > 10) {
                tweets.remove(0);
            }
        }

        public List<Integer> getNewsFeed(int userId) {
            // 确保用户存在
            if (!userFollows.containsKey(userId)) {
                userFollows.put(userId, new HashSet<>());
                userFollows.get(userId).add(userId); // 默认关注自己
                userTweets.put(userId, new ArrayList<>());
                return new ArrayList<>();
            }

            // 使用最大堆合并多个有序列表
            PriorityQueue<Object[]> heap = new PriorityQueue<>(
                    (a, b) -> Integer.compare((Integer) b[0], (Integer) a[0])
            );

            for (int followeeId : userFollows.get(userId)) {
                List<Tweet> tweets = userTweets.get(followeeId);
                if (tweets != null && !tweets.isEmpty()) {
                    // 为每个用户添加最新的推文及其索引
                    int idx = tweets.size() - 1;
                    Tweet tweet = tweets.get(idx);
                    heap.offer(new Object[]{tweet.time, tweet.id, followeeId, idx - 1});
                }
            }

            List<Integer> result = new ArrayList<>();
            while (!heap.isEmpty() && result.size() < 10) {
                Object[] top = heap.poll();
                result.add((Integer) top[1]);

                // 如果该用户还有更早的推文，添加到堆中
                int nextIdx = (Integer) top[3];
                if (nextIdx >= 0) {
                    int userId = (Integer) top[2];
                    Tweet nextTweet = userTweets.get(userId).get(nextIdx);
                    heap.offer(new Object[]{nextTweet.time, nextTweet.id, userId, nextIdx - 1});
                }
            }

            return result;
        }

        public void follow(int followerId, int followeeId) {
            // 确保两个用户都存在
            if (!userFollows.containsKey(followerId)) {
                userFollows.put(followerId, new HashSet<>());
                userFollows.get(followerId).add(followerId); // 默认关注自己
                userTweets.put(followerId, new ArrayList<>());
            }

            if (!userFollows.containsKey(followeeId)) {
                userFollows.put(followeeId, new HashSet<>());
                userFollows.get(followeeId).add(followeeId); // 默认关注自己
                userTweets.put(followeeId, new ArrayList<>());
            }

            // 添加关注关系
            userFollows.get(followerId).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            // 确保关注者存在
            if (!userFollows.containsKey(followerId)) {
                userFollows.put(followerId, new HashSet<>());
                userFollows.get(followerId).add(followerId); // 默认关注自己
                userTweets.put(followerId, new ArrayList<>());
                return;
            }

            // 确保被关注者存在
            if (!userFollows.containsKey(followeeId)) {
                userFollows.put(followeeId, new HashSet<>());
                userFollows.get(followeeId).add(followeeId); // 默认关注自己
                userTweets.put(followeeId, new ArrayList<>());
                return;
            }

            // 不能取消关注自己
            if (followerId != followeeId) {
                userFollows.get(followerId).remove(followeeId);
            }
        }
    }

    /**
     * 测试Twitter实现
     */
    public static void main(String[] args) {
        System.out.println("=== 测试Twitter类 ===");

        // 测试用例1: 基本功能测试
        System.out.println("测试用例1: 基本功能测试");
        Twitter twitter = new Twitter();
        twitter.postTweet(1, 5);  // 用户1发布推文5
        System.out.println(twitter.getNewsFeed(1));  // 应该返回 [5]
        twitter.follow(1, 2);  // 用户1关注用户2
        twitter.postTweet(2, 6);  // 用户2发布推文6
        System.out.println(twitter.getNewsFeed(1));  // 应该返回 [6, 5]
        twitter.unfollow(1, 2);  // 用户1取消关注用户2
        System.out.println(twitter.getNewsFeed(1));  // 应该返回 [5]

        // 测试用例2: AlternativeTwitter实现测试
        System.out.println("\n测试用例2: AlternativeTwitter实现测试");
        AlternativeTwitter altTwitter = new AlternativeTwitter();
        altTwitter.postTweet(1, 5);
        altTwitter.postTweet(2, 6);
        altTwitter.follow(1, 2);
        System.out.println(altTwitter.getNewsFeed(1));  // 应该返回 [6, 5]

        // 测试用例3: OptimizedTwitter实现测试
        System.out.println("\n测试用例3: OptimizedTwitter实现测试");
        OptimizedTwitter optTwitter = new OptimizedTwitter();
        optTwitter.postTweet(1, 5);
        optTwitter.postTweet(2, 6);
        optTwitter.follow(1, 2);
        System.out.println(optTwitter.getNewsFeed(1));  // 应该返回 [6, 5]
    }
}