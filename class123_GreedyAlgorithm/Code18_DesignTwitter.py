#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'''
LeetCode 355. 设计推特
题目链接：https://leetcode.cn/problems/design-twitter/
难度：中等

问题描述：
设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，以及查看最近 10 条推文。

实现 Twitter 类：
1. Twitter() 初始化简易版推特对象
2. void postTweet(int userId, int tweetId) 根据给定的 userId 和 tweetId 创建一条新推文。每次调用此函数都会使用一个不同的 tweetId。
3. List<Integer> getNewsFeed(int userId) 检索当前用户及其关注人最近 10 条推文，按时间顺序由近到远排序。
4. void follow(int followerId, int followeeId) ID 为 followerId 的用户开始关注 ID 为 followeeId 的用户。
5. void unfollow(int followerId, int followeeId) ID 为 followerId 的用户不再关注 ID 为 followeeId 的用户。

解题思路：
贪心算法 + 优先队列（堆）
1. 使用用户ID到其发布推文列表的映射
2. 使用用户ID到其关注列表的映射
3. 对于获取推文功能，使用优先队列按时间顺序合并多个有序列表
4. 使用全局计数器模拟时间戳

时间复杂度分析：
- postTweet: O(1)
- follow/unfollow: O(1)
- getNewsFeed: O(k log k + 10 log k)，其中k是关注的人数

空间复杂度分析：
- O(n + m)，其中n是用户数，m是推文数

最优性证明：
使用优先队列来合并多个有序列表是最优的方法，可以保证每次都取出最新的推文，直到获取10条或没有更多推文。
'''

import heapq
from typing import List
from collections import defaultdict

class Twitter:
    def __init__(self):
        """
        初始化Twitter对象
        
        数据结构设计：
        - user_tweets: 存储用户发布的推文，键为用户ID，值为推文列表
        - user_follows: 存储用户的关注关系，键为关注者ID，值为被关注者ID集合
        - time_counter: 全局时间戳计数器
        """
        self.user_tweets = defaultdict(list)  # 用户ID -> 推文列表[(time, tweetId), ...]
        self.user_follows = defaultdict(set)  # 用户ID -> 关注的用户ID集合
        self.time_counter = 0  # 全局时间戳
        self.MAX_TWEETS = 10  # 最多获取的推文数量
    
    def postTweet(self, userId: int, tweetId: int) -> None:
        """
        发布一条新推文
        
        Args:
            userId: 用户ID
            tweetId: 推文ID
        """
        # 将推文添加到用户的推文列表，使用负时间戳以便在堆中按降序排列
        # 负号是因为Python的heapq是最小堆，我们需要最大堆效果
        self.user_tweets[userId].append((-self.time_counter, tweetId))
        self.time_counter += 1
    
    def getNewsFeed(self, userId: int) -> List[int]:
        """
        获取用户及其关注者的最近10条推文
        
        Args:
            userId: 用户ID
            
        Returns:
            按时间倒序排列的推文ID列表
        """
        result = []
        heap = []
        
        # 添加用户自己的推文
        if userId in self.user_tweets and self.user_tweets[userId]:
            # 获取用户的最新推文
            time, tweetId = self.user_tweets[userId][-1]
            # 存储 (时间戳, 推文ID, 用户ID, 推文索引)
            heapq.heappush(heap, (time, tweetId, userId, len(self.user_tweets[userId]) - 1))
        
        # 添加用户关注的人的推文
        for followee_id in self.user_follows.get(userId, set()):
            if followee_id in self.user_tweets and self.user_tweets[followee_id]:
                # 获取关注用户的最新推文
                time, tweetId = self.user_tweets[followee_id][-1]
                heapq.heappush(heap, (time, tweetId, followee_id, len(self.user_tweets[followee_id]) - 1))
        
        # 从堆中取出最多10条最新推文
        count = 0
        while heap and count < self.MAX_TWEETS:
            time, tweetId, user_id, idx = heapq.heappop(heap)
            result.append(tweetId)
            count += 1
            
            # 如果该用户还有更早的推文，将其添加到堆中
            if idx > 0:
                prev_time, prev_tweet = self.user_tweets[user_id][idx - 1]
                heapq.heappush(heap, (prev_time, prev_tweet, user_id, idx - 1))
        
        return result
    
    def follow(self, followerId: int, followeeId: int) -> None:
        """
        用户关注另一个用户
        
        Args:
            followerId: 关注者ID
            followeeId: 被关注者ID
        """
        # 不能关注自己
        if followerId != followeeId:
            self.user_follows[followerId].add(followeeId)
    
    def unfollow(self, followerId: int, followeeId: int) -> None:
        """
        用户取消关注另一个用户
        
        Args:
            followerId: 关注者ID
            followeeId: 被关注者ID
        """
        # 如果关注关系存在，则取消关注
        if followerId in self.user_follows and followeeId in self.user_follows[followerId]:
            self.user_follows[followerId].remove(followeeId)

# 测试代码
def test_twitter():
    print("开始测试Twitter类...")
    
    # 初始化测试
    twitter = Twitter()
    print("初始化完成")
    
    # 测试用例1：基本功能测试
    print("\n测试用例1：基本功能测试")
    twitter.postTweet(1, 5)  # 用户1发布推文5
    feed1 = twitter.getNewsFeed(1)  # 应该返回 [5]
    print(f"用户1的推文流: {feed1}")
    assert feed1 == [5], f"测试用例1失败，期望[5]，得到{feed1}"
    
    # 测试用例2：关注和取消关注
    print("\n测试用例2：关注和取消关注")
    twitter.follow(1, 2)  # 用户1关注用户2
    print("用户1关注了用户2")
    twitter.postTweet(2, 6)  # 用户2发布推文6
    print("用户2发布了推文6")
    feed2 = twitter.getNewsFeed(1)  # 应该返回 [6, 5]
    print(f"用户1的推文流: {feed2}")
    assert feed2 == [6, 5], f"测试用例2失败，期望[6, 5]，得到{feed2}"
    
    twitter.unfollow(1, 2)  # 用户1取消关注用户2
    print("用户1取消关注了用户2")
    feed3 = twitter.getNewsFeed(1)  # 应该返回 [5]
    print(f"用户1的推文流: {feed3}")
    assert feed3 == [5], f"测试用例3失败，期望[5]，得到{feed3}"
    
    # 测试用例3：多个用户发布多条推文
    print("\n测试用例3：多个用户发布多条推文")
    twitter.postTweet(1, 7)
    twitter.postTweet(1, 8)
    print("用户1发布了推文7和8")
    twitter.follow(1, 3)
    print("用户1关注了用户3")
    twitter.postTweet(3, 9)
    twitter.postTweet(3, 10)
    print("用户3发布了推文9和10")
    feed4 = twitter.getNewsFeed(1)  # 应该返回 [10, 9, 8, 7, 5]
    print(f"用户1的推文流: {feed4}")
    assert feed4 == [10, 9, 8, 7, 5], f"测试用例4失败，期望[10, 9, 8, 7, 5]，得到{feed4}"
    
    # 测试用例4：边界情况 - 获取超过10条推文
    print("\n测试用例4：边界情况 - 获取超过10条推文")
    twitter.postTweet(1, 11)
    twitter.postTweet(1, 12)
    twitter.postTweet(1, 13)
    twitter.postTweet(1, 14)
    twitter.postTweet(1, 15)
    twitter.postTweet(1, 16)
    twitter.postTweet(1, 17)
    print("用户1发布了推文11-17")
    feed5 = twitter.getNewsFeed(1)  # 应该只返回最近的10条
    print(f"用户1的推文流: {feed5}")
    print(f"结果长度: {len(feed5)}")
    assert len(feed5) == 10, f"测试用例5失败，期望长度10，得到{len(feed5)}"
    
    # 测试用例5：关注自己的情况
    print("\n测试用例5：关注自己的情况")
    twitter.follow(1, 1)  # 尝试关注自己
    feed6 = twitter.getNewsFeed(1)
    print(f"关注自己后，用户1的推文流长度: {len(feed6)}")
    # 关注自己应该不会有变化，因为代码中禁止了自关注
    
    print("\n所有测试通过！")

# 性能测试
def performance_test():
    print("\n开始性能测试...")
    import time
    
    twitter = Twitter()
    
    # 创建100个用户，每个用户发布10条推文
    start_time = time.time()
    for user_id in range(1, 101):
        for tweet_id in range(user_id * 10, (user_id + 1) * 10):
            twitter.postTweet(user_id, tweet_id)
    end_time = time.time()
    print(f"创建100个用户，每个用户发布10条推文，耗时: {end_time - start_time:.4f}秒")
    
    # 用户1关注50个其他用户
    start_time = time.time()
    for followee_id in range(2, 52):
        twitter.follow(1, followee_id)
    end_time = time.time()
    print(f"用户1关注50个其他用户，耗时: {end_time - start_time:.4f}秒")
    
    # 获取用户1的推文流（包含50个用户的推文）
    start_time = time.time()
    feed = twitter.getNewsFeed(1)
    end_time = time.time()
    print(f"获取包含50个用户推文的推文流，耗时: {end_time - start_time:.4f}秒")
    print(f"获取到的推文数量: {len(feed)}")

if __name__ == "__main__":
    test_twitter()
    performance_test()

"""
Python语言特性与优化：
1. 使用defaultdict自动初始化不存在的键，简化代码
2. 利用Python的heapq模块实现优先队列
3. 使用负时间戳将最小堆转换为最大堆效果
4. 使用元组存储复合数据，便于堆排序
5. 使用类型提示增强代码可读性和IDE支持

工程化建议：
1. 代码中包含详细的文档字符串，说明每个方法的功能、参数和返回值
2. 提供完整的测试用例，包括基本功能测试和边界情况测试
3. 实现性能测试，评估算法在大规模数据上的表现
4. 变量命名清晰，符合Python的PEP 8规范
5. 适当使用注释解释复杂的逻辑

扩展功能建议：
1. 添加用户验证机制
2. 实现推文删除功能
3. 添加推文点赞和评论功能
4. 实现热门推文推荐功能
5. 支持按时间范围查询推文

Python特有的优化：
1. 对于大规模数据，可以考虑使用更高效的数据结构，如使用deque存储推文
2. 可以使用functools.lru_cache装饰器缓存热点用户的推文流
3. 使用异步编程提高并发性能
4. 对于分布式系统，可以考虑使用Redis等缓存数据库

调试技巧：
1. 在关键操作处添加print语句打印状态信息
2. 使用logging模块记录运行日志
3. 使用unittest模块编写更规范的单元测试
4. 使用cProfile分析性能瓶颈
"""