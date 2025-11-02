"""
LeetCode 355. 设计推特 (Design Twitter)
题目链接：https://leetcode.com/problems/design-twitter/

题目描述：
设计一个简化版的推特(Twitter)，可以让用户发送推文，关注/取消关注其他用户，
能够看见关注人（包括自己）的最近10条推文。

实现 Twitter 类：
- Twitter() 初始化简易版推特对象
- void postTweet(int userId, int tweetId) 根据给定的 tweetId 和 userId 创建一条新推文
- List<Integer> getNewsFeed(int userId) 检索当前用户新闻推送中最近 10 条推文的 ID
- void follow(int followerId, int followeeId) ID 为 followerId 的用户开始关注 ID 为 followeeId 的用户
- void unfollow(int followerId, int followeeId) ID 为 followerId 的用户不再关注 ID 为 followeeId 的用户

算法思路：
1. 使用哈希表存储用户信息和关注关系
2. 使用全局时间戳确保推文按时间排序
3. 使用优先队列(堆)合并多个用户的推文流
4. 限制每个用户的推文数量以节省空间

时间复杂度：
- postTweet: O(1)
- getNewsFeed: O(n*log(k))，n为关注用户数，k为每个用户最近推文数
- follow: O(1)
- unfollow: O(1)

空间复杂度：O(U + T)，U为用户数，T为推文数

工程化考量：
1. 线程安全：使用并发数据结构保证多线程环境下的正确性
2. 内存优化：限制每个用户保存的推文数量
3. 性能优化：使用堆合并多个有序推文流
4. 异常处理：处理非法用户ID和操作
"""

import heapq
import threading
import time
from collections import defaultdict
from typing import List, Dict, Set, Tuple, Any
from dataclasses import dataclass
from heapq import heappush, heappop
import queue


@dataclass
class Tweet:
    """推文类"""
    tweet_id: int
    user_id: int
    timestamp: int


class User:
    """用户类"""
    
    def __init__(self, user_id: int):
        self.user_id = user_id
        self.following: Set[int] = {user_id}  # 关注的用户，初始关注自己
        self.tweets: List[Tweet] = []  # 发送的推文


class Code24_LeetCode355_DesignTwitter:
    """设计推特系统实现"""
    
    # 最大保存的推文数量
    MAX_TWEETS_PER_USER = 100
    
    def __init__(self):
        """初始化推特系统"""
        self.timestamp = 0
        self.users: Dict[int, User] = {}
        self.lock = threading.RLock()  # 可重入读写锁
    
    def postTweet(self, userId: int, tweetId: int) -> None:
        """
        用户发送推文
        :param userId: 用户ID
        :param tweetId: 推文ID
        """
        with self.lock:
            # 获取或创建用户
            if userId not in self.users:
                self.users[userId] = User(userId)
            user = self.users[userId]
            
            # 创建推文
            self.timestamp += 1
            tweet = Tweet(tweetId, userId, self.timestamp)
            
            # 添加推文到用户
            user.tweets.append(tweet)
            
            # 限制推文数量，移除最旧的推文
            if len(user.tweets) > self.MAX_TWEETS_PER_USER:
                user.tweets.pop(0)
    
    def getNewsFeed(self, userId: int) -> List[int]:
        """
        获取用户新闻推送（最近10条推文）
        :param userId: 用户ID
        :return: 最近10条推文ID列表
        """
        with self.lock:
            # 获取用户
            if userId not in self.users:
                return []
            user = self.users[userId]
            
            # 使用优先队列合并多个有序推文流
            # 大顶堆，按时间戳排序（使用负数实现大顶堆）
            max_heap = []
            
            # 将关注用户的最近推文加入堆中
            for followee_id in user.following:
                if followee_id in self.users:
                    followee = self.users[followee_id]
                    if followee.tweets:
                        # 只添加最近的推文
                        start = max(0, len(followee.tweets) - 10)
                        for i in range(start, len(followee.tweets)):
                            tweet = followee.tweets[i]
                            # 使用负时间戳实现大顶堆
                            heapq.heappush(max_heap, (-tweet.timestamp, tweet.tweet_id))
            
            # 获取最近10条推文
            news_feed = []
            count = 0
            while max_heap and count < 10:
                _, tweet_id = heapq.heappop(max_heap)
                news_feed.append(tweet_id)
                count += 1
            
            return news_feed
    
    def follow(self, followerId: int, followeeId: int) -> None:
        """
        用户关注另一个用户
        :param followerId: 关注者ID
        :param followeeId: 被关注者ID
        """
        # 不能关注自己（除了初始化时）
        if followerId == followeeId:
            return
        
        with self.lock:
            # 获取或创建关注者
            if followerId not in self.users:
                self.users[followerId] = User(followerId)
            
            # 获取或创建被关注者
            if followeeId not in self.users:
                self.users[followeeId] = User(followeeId)
            
            # 添加关注关系
            self.users[followerId].following.add(followeeId)
    
    def unfollow(self, followerId: int, followeeId: int) -> None:
        """
        用户取消关注另一个用户
        :param followerId: 关注者ID
        :param followeeId: 被关注者ID
        """
        # 不能取消关注自己
        if followerId == followeeId:
            return
        
        with self.lock:
            if followerId in self.users:
                self.users[followerId].following.discard(followeeId)
    
    def getStatistics(self) -> Dict[str, Any]:
        """
        获取系统统计信息
        :return: 统计信息映射
        """
        with self.lock:
            stats: Dict[str, Any] = {
                "userCount": len(self.users),
                "totalTweets": 0,
                "totalFollowing": 0,
                "avgTweetsPerUser": 0.0,
                "avgFollowingPerUser": 0.0
            }
            
            for user in self.users.values():
                stats["totalTweets"] += len(user.tweets)
                stats["totalFollowing"] += len(user.following)
            
            if self.users:
                stats["avgTweetsPerUser"] = float(stats["totalTweets"] / len(self.users))
                stats["avgFollowingPerUser"] = float(stats["totalFollowing"] / len(self.users))
            
            return stats


class PerformanceTest:
    """性能测试类"""
    
    @staticmethod
    def testTwitterPerformance(twitter: Code24_LeetCode355_DesignTwitter, 
                               userCount: int, tweetCount: int) -> None:
        """
        测试推特系统的性能
        :param twitter: 推特系统实例
        :param userCount: 用户数量
        :param tweetCount: 推文数量
        """
        print("=== 推特系统性能测试 ===")
        print(f"用户数量: {userCount}, 推文数量: {tweetCount}")
        
        import random
        random.seed(42)
        
        # 测试用户操作性能
        start_time = time.perf_counter()
        
        # 创建用户并发送推文
        for i in range(userCount):
            userId = i + 1
            for j in range(tweetCount // userCount):
                twitter.postTweet(userId, userId * 1000 + j)
        
        post_time = time.perf_counter() - start_time
        
        # 测试关注关系
        start_time = time.perf_counter()
        for i in range(userCount):
            userId = i + 1
            # 每个用户关注5个随机用户
            for j in range(5):
                followeeId = random.randint(1, userCount)
                twitter.follow(userId, followeeId)
        
        follow_time = time.perf_counter() - start_time
        
        # 测试获取新闻推送
        start_time = time.perf_counter()
        for i in range(1000):
            userId = random.randint(1, userCount)
            twitter.getNewsFeed(userId)
        
        get_feed_time = time.perf_counter() - start_time
        
        print(f"发送推文平均时间: {post_time / tweetCount * 1e9:.2f} ns")
        print(f"建立关注关系平均时间: {follow_time / (userCount * 5) * 1e9:.2f} ns")
        print(f"获取新闻推送平均时间: {get_feed_time / 1000 * 1e9:.2f} ns")
        
        # 显示统计信息
        print("系统统计信息:", twitter.getStatistics())


def main():
    """主函数"""
    print("=== 设计推特系统测试 ===\n")
    
    twitter = Code24_LeetCode355_DesignTwitter()
    
    # 基本功能测试
    print("1. 基本功能测试:")
    
    # 用户1发送推文
    twitter.postTweet(1, 5)
    print("用户1发送推文5")
    
    # 用户1获取新闻推送
    feed = twitter.getNewsFeed(1)
    print("用户1新闻推送:", feed)
    
    # 用户1关注用户2
    twitter.follow(1, 2)
    print("用户1关注用户2")
    
    # 用户2发送推文
    twitter.postTweet(2, 6)
    print("用户2发送推文6")
    
    # 用户1获取新闻推送
    feed = twitter.getNewsFeed(1)
    print("用户1新闻推送:", feed)
    
    # 用户1取消关注用户2
    twitter.unfollow(1, 2)
    print("用户1取消关注用户2")
    
    # 用户1获取新闻推送
    feed = twitter.getNewsFeed(1)
    print("用户1新闻推送:", feed)
    
    # 复杂场景测试
    print("\n2. 复杂场景测试:")
    
    # 创建多个用户和推文
    for i in range(1, 4):
        for j in range(1, 6):
            twitter.postTweet(i, i * 100 + j)
    
    # 建立关注关系
    twitter.follow(1, 2)
    twitter.follow(1, 3)
    
    # 用户1获取新闻推送
    feed = twitter.getNewsFeed(1)
    print("用户1新闻推送:", feed)
    
    # 性能测试
    print("\n3. 性能测试:")
    twitter2 = Code24_LeetCode355_DesignTwitter()
    PerformanceTest.testTwitterPerformance(twitter2, 100, 1000)
    
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度:")
    print("- postTweet: O(1)")
    print("- getNewsFeed: O(n*log(k))，n为关注用户数，k为每个用户最近推文数")
    print("- follow: O(1)")
    print("- unfollow: O(1)")
    print("空间复杂度: O(U + T)，U为用户数，T为推文数")
    
    print("\n=== 工程化应用场景 ===")
    print("1. 社交媒体平台: Twitter、微博等社交平台的消息系统")
    print("2. 内容推荐系统: 基于用户关注关系的内容推荐")
    print("3. 新闻聚合系统: 合并多个信息源的新闻内容")
    print("4. 实时消息系统: 即时通讯应用中的消息推送")
    
    print("\n=== 设计要点 ===")
    print("1. 数据结构选择: 哈希表存储用户和关注关系，堆合并推文流")
    print("2. 内存优化: 限制每个用户保存的推文数量")
    print("3. 并发安全: 使用读写锁保证多线程环境下的正确性")
    print("4. 时间排序: 使用全局时间戳确保推文按时间排序")


if __name__ == "__main__":
    main()