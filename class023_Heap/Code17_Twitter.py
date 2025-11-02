#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
LeetCode 355: 设计推特

设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，
以及能够看见关注人（包括自己）的最近10条推文。

解题思路：
1. 使用字典存储用户信息、关注列表和推文
2. 使用堆来按时间线合并推文
3. 使用一个全局计数器模拟时间戳
"""

import heapq
from typing import List, Dict, Set


class Twitter:
    """
    推特类，实现了用户发布推文、关注/取消关注用户、获取最新动态等功能
    使用堆来高效获取最新动态
    """
    
    def __init__(self):
        """
        初始化Twitter对象
        
        - users: 存储用户ID到用户信息的映射
        - follows: 存储用户ID到其关注的用户ID集合的映射
        - tweets: 存储用户ID到其发布的推文列表的映射
        - global_time: 全局计数器，用于记录推文的发布时间顺序
        """
        self.users = dict()  # 用户信息映射
        self.follows = dict()  # 用户关注关系
        self.tweets = dict()  # 用户推文存储
        self.global_time = 0  # 全局时间戳
    
    def postTweet(self, userId: int, tweetId: int) -> None:
        """
        用户发布一条新推文
        
        Args:
            userId: 用户ID
            tweetId: 推文ID
        """
        # 确保用户存在
        if userId not in self.users:
            self.users[userId] = True
            self.follows[userId] = {userId}  # 默认关注自己
            self.tweets[userId] = []
        
        # 发布推文，使用负时间戳以便在堆中按时间倒序排列
        self.tweets[userId].append((-self.global_time, tweetId))
        self.global_time += 1
    
    def getNewsFeed(self, userId: int) -> List[int]:
        """
        获取用户关注的所有人（包括自己）发布的最近10条推文
        
        Args:
            userId: 用户ID
            
        Returns:
            List[int]: 按发布时间倒序排列的最近10条推文ID列表
        """
        # 确保用户存在
        if userId not in self.users:
            self.users[userId] = True
            self.follows[userId] = {userId}
            self.tweets[userId] = []
            return []
        
        # 使用最大堆来获取最新的推文
        # 堆中每个元素是: (-时间戳, 推文ID, 用户ID, 该用户下一条推文的索引)
        max_heap = []
        
        # 初始化堆，为每个关注的用户添加最新的推文
        for followee_id in self.follows[userId]:
            if followee_id in self.tweets and self.tweets[followee_id]:
                # 获取该用户最新的推文（最后发布的）
                last_idx = len(self.tweets[followee_id]) - 1
                time, tweet_id = self.tweets[followee_id][last_idx]
                max_heap.append((time, tweet_id, followee_id, last_idx - 1))
        
        # 构建最大堆
        heapq.heapify(max_heap)
        
        # 获取最近的10条推文
        result = []
        while max_heap and len(result) < 10:
            time, tweet_id, user_id, next_idx = heapq.heappop(max_heap)
            result.append(tweet_id)
            
            # 如果该用户还有更早的推文，添加到堆中
            if next_idx >= 0:
                next_time, next_tweet_id = self.tweets[user_id][next_idx]
                heapq.heappush(max_heap, (next_time, next_tweet_id, user_id, next_idx - 1))
        
        return result
    
    def follow(self, followerId: int, followeeId: int) -> None:
        """
        用户关注另一个用户
        
        Args:
            followerId: 关注者ID
            followeeId: 被关注者ID
        """
        # 确保两个用户都存在
        if followerId not in self.users:
            self.users[followerId] = True
            self.follows[followerId] = {followerId}
            self.tweets[followerId] = []
        
        if followeeId not in self.users:
            self.users[followeeId] = True
            self.follows[followeeId] = {followeeId}
            self.tweets[followeeId] = []
        
        # 添加关注关系
        self.follows[followerId].add(followeeId)
    
    def unfollow(self, followerId: int, followeeId: int) -> None:
        """
        用户取消关注另一个用户
        
        Args:
            followerId: 关注者ID
            followeeId: 被关注者ID
        """
        # 确保关注者存在
        if followerId not in self.users:
            self.users[followerId] = True
            self.follows[followerId] = {followerId}
            self.tweets[followerId] = []
            return
        
        # 确保被关注者存在
        if followeeId not in self.users:
            self.users[followeeId] = True
            self.follows[followeeId] = {followeeId}
            self.tweets[followeeId] = []
            return
        
        # 不能取消关注自己
        if followerId != followeeId:
            self.follows[followerId].discard(followeeId)


class AlternativeTwitter:
    """
    推特类的另一种实现，使用更简单的数据结构
    适用于了解基本功能实现
    """
    
    def __init__(self):
        self.tweets = []  # 存储所有推文 (时间戳, userID, tweetID)
        self.follows = dict()  # 存储用户关注关系
        self.time = 0
    
    def postTweet(self, userId: int, tweetId: int) -> None:
        # 确保用户存在
        if userId not in self.follows:
            self.follows[userId] = {userId}
        
        # 添加推文
        self.tweets.append((-self.time, userId, tweetId))
        self.time += 1
    
    def getNewsFeed(self, userId: int) -> List[int]:
        # 确保用户存在
        if userId not in self.follows:
            self.follows[userId] = {userId}
            return []
        
        # 筛选出关注的用户的推文
        result = []
        for time, user_id, tweet_id in sorted(self.tweets):
            if user_id in self.follows[userId]:
                result.append(tweet_id)
                if len(result) == 10:
                    break
        
        return result
    
    def follow(self, followerId: int, followeeId: int) -> None:
        # 确保两个用户都存在
        if followerId not in self.follows:
            self.follows[followerId] = {followerId}
        
        if followeeId not in self.follows:
            self.follows[followeeId] = {followeeId}
        
        # 添加关注关系
        self.follows[followerId].add(followeeId)
    
    def unfollow(self, followerId: int, followeeId: int) -> None:
        # 确保关注者存在
        if followerId not in self.follows:
            self.follows[followerId] = {followerId}
            return
        
        # 不能取消关注自己
        if followerId != followeeId:
            self.follows[followerId].discard(followeeId)


class OptimizedTwitter:
    """
    优化版的Twitter实现，针对大规模数据优化了性能
    使用更高效的数据结构和算法
    """
    
    def __init__(self):
        self.user_tweets = dict()  # 用户ID -> 最近的推文列表，限制存储最近10条
        self.user_follows = dict()  # 用户ID -> 关注的用户ID集合
        self.time = 0
    
    def postTweet(self, userId: int, tweetId: int) -> None:
        # 确保用户存在
        if userId not in self.user_tweets:
            self.user_tweets[userId] = []
            self.user_follows[userId] = {userId}
        
        # 添加推文，只保留最近的10条
        self.user_tweets[userId].append((-self.time, tweetId))
        if len(self.user_tweets[userId]) > 10:
            self.user_tweets[userId].pop(0)
        
        self.time += 1
    
    def getNewsFeed(self, userId: int) -> List[int]:
        # 确保用户存在
        if userId not in self.user_follows:
            self.user_follows[userId] = {userId}
            self.user_tweets[userId] = []
            return []
        
        # 使用堆合并多个有序列表
        heap = []
        for followee_id in self.user_follows[userId]:
            if followee_id in self.user_tweets and self.user_tweets[followee_id]:
                # 为每个用户添加最新的推文及其索引
                tweets = self.user_tweets[followee_id]
                # 从最近的推文开始（最后一个元素）
                idx = len(tweets) - 1
                time, tweet_id = tweets[idx]
                heap.append((time, tweet_id, followee_id, idx - 1))
        
        heapq.heapify(heap)
        
        result = []
        while heap and len(result) < 10:
            time, tweet_id, user_id, next_idx = heapq.heappop(heap)
            result.append(tweet_id)
            
            # 如果该用户还有更早的推文，添加到堆中
            if next_idx >= 0:
                next_time, next_tweet_id = self.user_tweets[user_id][next_idx]
                heapq.heappush(heap, (next_time, next_tweet_id, user_id, next_idx - 1))
        
        return result
    
    def follow(self, followerId: int, followeeId: int) -> None:
        # 确保两个用户都存在
        if followerId not in self.user_follows:
            self.user_follows[followerId] = {followerId}
            self.user_tweets[followerId] = []
        
        if followeeId not in self.user_follows:
            self.user_follows[followeeId] = {followeeId}
            self.user_tweets[followeeId] = []
        
        # 添加关注关系
        self.user_follows[followerId].add(followeeId)
    
    def unfollow(self, followerId: int, followeeId: int) -> None:
        # 确保关注者存在
        if followerId not in self.user_follows:
            self.user_follows[followerId] = {followerId}
            self.user_tweets[followerId] = []
            return
        
        # 确保被关注者存在
        if followeeId not in self.user_follows:
            self.user_follows[followeeId] = {followeeId}
            self.user_tweets[followeeId] = []
            return
        
        # 不能取消关注自己
        if followerId != followeeId:
            self.user_follows[followerId].discard(followeeId)


def test_twitter_implementation():
    """
    测试Twitter类的各种功能
    """
    print("=== 测试Twitter类 ===")
    
    # 测试用例1: 基本功能测试
    print("测试用例1: 基本功能测试")
    twitter = Twitter()
    twitter.postTweet(1, 5)  # 用户1发布推文5
    print(twitter.getNewsFeed(1))  # 应该返回 [5]
    twitter.follow(1, 2)  # 用户1关注用户2
    twitter.postTweet(2, 6)  # 用户2发布推文6
    print(twitter.getNewsFeed(1))  # 应该返回 [6, 5]
    twitter.unfollow(1, 2)  # 用户1取消关注用户2
    print(twitter.getNewsFeed(1))  # 应该返回 [5]
    
    # 测试用例2: 关注多个用户
    print("\n测试用例2: 关注多个用户")
    twitter = Twitter()
    twitter.postTweet(1, 101)
    twitter.postTweet(2, 201)
    twitter.postTweet(3, 301)
    twitter.follow(4, 1)  # 用户4关注用户1
    twitter.follow(4, 2)  # 用户4关注用户2
    twitter.follow(4, 3)  # 用户4关注用户3
    twitter.postTweet(1, 102)
    twitter.postTweet(2, 202)
    print(twitter.getNewsFeed(4))  # 应该返回 [202, 102, 301, 201, 101] 的前10条
    
    # 测试用例3: 超过10条推文
    print("\n测试用例3: 超过10条推文")
    twitter = Twitter()
    for i in range(15):
        twitter.postTweet(1, 100 + i)
    print(twitter.getNewsFeed(1))  # 应该返回最近的10条推文
    
    # 测试用例4: 边界情况
    print("\n测试用例4: 边界情况")
    twitter = Twitter()
    print(twitter.getNewsFeed(999))  # 不存在的用户，应该返回 []
    twitter.follow(5, 6)  # 两个不存在的用户
    twitter.postTweet(5, 501)
    print(twitter.getNewsFeed(5))  # 应该返回 [501]
    
    # 测试用例5: AlternativeTwitter实现测试
    print("\n测试用例5: AlternativeTwitter实现测试")
    alt_twitter = AlternativeTwitter()
    alt_twitter.postTweet(1, 5)
    print(alt_twitter.getNewsFeed(1))  # 应该返回 [5]
    
    # 测试用例6: OptimizedTwitter实现测试
    print("\n测试用例6: OptimizedTwitter实现测试")
    opt_twitter = OptimizedTwitter()
    opt_twitter.postTweet(1, 5)
    opt_twitter.follow(1, 2)
    opt_twitter.postTweet(2, 6)
    print(opt_twitter.getNewsFeed(1))  # 应该返回 [6, 5]
    
    print("\n所有测试用例执行完毕！")


if __name__ == "__main__":
    test_twitter_implementation()