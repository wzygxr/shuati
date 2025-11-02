#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <queue>
#include <memory>

/**
 * LeetCode 355: 设计推特
 * 
 * 设计一个简化版的推特(Twitter)，可以让用户实现发送推文，关注/取消关注其他用户，
 * 以及能够看见关注人（包括自己）的最近10条推文。
 * 
 * 解题思路：
 * 1. 使用字典存储用户信息、关注列表和推文
 * 2. 使用优先队列（堆）来按时间线合并推文
 * 3. 使用一个全局计数器模拟时间戳
 */

class Twitter {
private:
    // 推文结构，存储推文ID和发布时间
    struct Tweet {
        int tweetId;
        int timestamp;
        
        Tweet(int id, int time) : tweetId(id), timestamp(time) {}
    };
    
    // 用户ID到其关注的用户ID集合的映射
    std::unordered_map<int, std::unordered_set<int>> follows;
    // 用户ID到其发布的推文列表的映射
    std::unordered_map<int, std::vector<Tweet>> tweets;
    // 全局时间戳，用于记录推文的发布顺序
    int globalTime;
    
public:
    /**
     * 初始化Twitter对象
     */
    Twitter() : globalTime(0) {
    }
    
    /**
     * 用户发布一条新推文
     * 
     * @param userId 用户ID
     * @param tweetId 推文ID
     */
    void postTweet(int userId, int tweetId) {
        // 确保用户存在
        if (follows.find(userId) == follows.end()) {
            follows[userId].insert(userId); // 默认关注自己
            tweets[userId] = std::vector<Tweet>();
        }
        
        // 发布推文
        tweets[userId].emplace_back(tweetId, globalTime);
        globalTime++;
    }
    
    /**
     * 获取用户关注的所有人（包括自己）发布的最近10条推文
     * 
     * @param userId 用户ID
     * @return 按发布时间倒序排列的最近10条推文ID列表
     */
    std::vector<int> getNewsFeed(int userId) {
        // 确保用户存在
        if (follows.find(userId) == follows.end()) {
            follows[userId].insert(userId); // 默认关注自己
            tweets[userId] = std::vector<Tweet>();
            return std::vector<int>();
        }
        
        // 定义优先队列的比较函数，使用最大堆（按时间戳降序）
        // 堆中每个元素是: {时间戳, 推文ID, 用户ID, 该用户下一条推文的索引}
        using Element = std::tuple<int, int, int, int>; // timestamp, tweetId, userId, nextIndex
        auto cmp = [](const Element& a, const Element& b) {
            return std::get<0>(a) < std::get<0>(b); // 最大堆，所以使用小于号
        };
        
        std::priority_queue<Element, std::vector<Element>, decltype(cmp)> maxHeap(cmp);
        
        // 初始化堆，为每个关注的用户添加最新的推文
        for (int followeeId : follows[userId]) {
            auto& userTweets = tweets[followeeId];
            if (!userTweets.empty()) {
                // 获取该用户最新的推文（最后发布的）
                int lastIdx = userTweets.size() - 1;
                maxHeap.emplace(
                    userTweets[lastIdx].timestamp,
                    userTweets[lastIdx].tweetId,
                    followeeId,
                    lastIdx - 1
                );
            }
        }
        
        // 获取最近的10条推文
        std::vector<int> result;
        while (!maxHeap.empty() && result.size() < 10) {
            auto [timestamp, tweetId, user, nextIdx] = maxHeap.top();
            maxHeap.pop();
            result.push_back(tweetId);
            
            // 如果该用户还有更早的推文，添加到堆中
            if (nextIdx >= 0) {
                auto& userTweets = tweets[user];
                maxHeap.emplace(
                    userTweets[nextIdx].timestamp,
                    userTweets[nextIdx].tweetId,
                    user,
                    nextIdx - 1
                );
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
    void follow(int followerId, int followeeId) {
        // 确保两个用户都存在
        if (follows.find(followerId) == follows.end()) {
            follows[followerId].insert(followerId); // 默认关注自己
            tweets[followerId] = std::vector<Tweet>();
        }
        
        if (follows.find(followeeId) == follows.end()) {
            follows[followeeId].insert(followeeId); // 默认关注自己
            tweets[followeeId] = std::vector<Tweet>();
        }
        
        // 添加关注关系
        follows[followerId].insert(followeeId);
    }
    
    /**
     * 用户取消关注另一个用户
     * 
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    void unfollow(int followerId, int followeeId) {
        // 确保关注者存在
        if (follows.find(followerId) == follows.end()) {
            follows[followerId].insert(followerId); // 默认关注自己
            tweets[followerId] = std::vector<Tweet>();
            return;
        }
        
        // 确保被关注者存在
        if (follows.find(followeeId) == follows.end()) {
            follows[followeeId].insert(followeeId); // 默认关注自己
            tweets[followeeId] = std::vector<Tweet>();
            return;
        }
        
        // 不能取消关注自己
        if (followerId != followeeId) {
            follows[followerId].erase(followeeId);
        }
    }
};

/**
 * Twitter类的另一种实现，使用更简单的数据结构
 * 适用于了解基本功能实现
 */
class AlternativeTwitter {
private:
    // 存储所有推文 (时间戳, userId, tweetId)
    std::vector<std::tuple<int, int, int>> allTweets;
    // 存储用户关注关系
    std::unordered_map<int, std::unordered_set<int>> follows;
    int time;
    
public:
    AlternativeTwitter() : time(0) {
    }
    
    void postTweet(int userId, int tweetId) {
        // 确保用户存在
        if (follows.find(userId) == follows.end()) {
            follows[userId].insert(userId); // 默认关注自己
        }
        
        // 添加推文
        allTweets.emplace_back(time++, userId, tweetId);
    }
    
    std::vector<int> getNewsFeed(int userId) {
        // 确保用户存在
        if (follows.find(userId) == follows.end()) {
            follows[userId].insert(userId); // 默认关注自己
            return std::vector<int>();
        }
        
        // 筛选出关注的用户的推文
        std::vector<int> result;
        // 从最近的推文开始遍历
        for (auto it = allTweets.rbegin(); it != allTweets.rend() && result.size() < 10; ++it) {
            auto [timestamp, user, tweetId] = *it;
            if (follows[userId].count(user)) {
                result.push_back(tweetId);
            }
        }
        
        return result;
    }
    
    void follow(int followerId, int followeeId) {
        // 确保两个用户都存在
        if (follows.find(followerId) == follows.end()) {
            follows[followerId].insert(followerId); // 默认关注自己
        }
        
        if (follows.find(followeeId) == follows.end()) {
            follows[followeeId].insert(followeeId); // 默认关注自己
        }
        
        // 添加关注关系
        follows[followerId].insert(followeeId);
    }
    
    void unfollow(int followerId, int followeeId) {
        // 确保关注者存在
        if (follows.find(followerId) == follows.end()) {
            follows[followerId].insert(followerId); // 默认关注自己
            return;
        }
        
        // 不能取消关注自己
        if (followerId != followeeId) {
            follows[followerId].erase(followeeId);
        }
    }
};

/**
 * 优化版的Twitter实现，针对大规模数据优化了性能
 * 使用更高效的数据结构和算法
 */
class OptimizedTwitter {
private:
    struct Tweet {
        int tweetId;
        int timestamp;
        
        Tweet(int id, int time) : tweetId(id), timestamp(time) {}
    };
    
    // 用户ID -> 最近的推文列表，限制存储最近10条
    std::unordered_map<int, std::vector<Tweet>> userTweets;
    // 用户ID -> 关注的用户ID集合
    std::unordered_map<int, std::unordered_set<int>> userFollows;
    int time;
    static constexpr int MAX_TWEETS_PER_USER = 10;
    
public:
    OptimizedTwitter() : time(0) {
    }
    
    void postTweet(int userId, int tweetId) {
        // 确保用户存在
        if (userFollows.find(userId) == userFollows.end()) {
            userFollows[userId].insert(userId); // 默认关注自己
            userTweets[userId] = std::vector<Tweet>();
        }
        
        // 添加推文，只保留最近的10条
        auto& tweets = userTweets[userId];
        tweets.emplace_back(tweetId, time++);
        if (tweets.size() > MAX_TWEETS_PER_USER) {
            tweets.erase(tweets.begin());
        }
    }
    
    std::vector<int> getNewsFeed(int userId) {
        // 确保用户存在
        if (userFollows.find(userId) == userFollows.end()) {
            userFollows[userId].insert(userId); // 默认关注自己
            userTweets[userId] = std::vector<Tweet>();
            return std::vector<int>();
        }
        
        // 使用最大堆合并多个有序列表
        using Element = std::tuple<int, int, int, int>; // timestamp, tweetId, userId, nextIndex
        auto cmp = [](const Element& a, const Element& b) {
            return std::get<0>(a) < std::get<0>(b); // 最大堆
        };
        
        std::priority_queue<Element, std::vector<Element>, decltype(cmp)> maxHeap(cmp);
        
        for (int followeeId : userFollows[userId]) {
            auto& tweets = userTweets[followeeId];
            if (!tweets.empty()) {
                // 为每个用户添加最新的推文及其索引
                int idx = tweets.size() - 1;
                maxHeap.emplace(
                    tweets[idx].timestamp,
                    tweets[idx].tweetId,
                    followeeId,
                    idx - 1
                );
            }
        }
        
        std::vector<int> result;
        while (!maxHeap.empty() && result.size() < 10) {
            auto [timestamp, tweetId, user, nextIdx] = maxHeap.top();
            maxHeap.pop();
            result.push_back(tweetId);
            
            // 如果该用户还有更早的推文，添加到堆中
            if (nextIdx >= 0) {
                auto& tweets = userTweets[user];
                maxHeap.emplace(
                    tweets[nextIdx].timestamp,
                    tweets[nextIdx].tweetId,
                    user,
                    nextIdx - 1
                );
            }
        }
        
        return result;
    }
    
    void follow(int followerId, int followeeId) {
        // 确保两个用户都存在
        if (userFollows.find(followerId) == userFollows.end()) {
            userFollows[followerId].insert(followerId); // 默认关注自己
            userTweets[followerId] = std::vector<Tweet>();
        }
        
        if (userFollows.find(followeeId) == userFollows.end()) {
            userFollows[followeeId].insert(followeeId); // 默认关注自己
            userTweets[followeeId] = std::vector<Tweet>();
        }
        
        // 添加关注关系
        userFollows[followerId].insert(followeeId);
    }
    
    void unfollow(int followerId, int followeeId) {
        // 确保关注者存在
        if (userFollows.find(followerId) == userFollows.end()) {
            userFollows[followerId].insert(followerId); // 默认关注自己
            userTweets[followerId] = std::vector<Tweet>();
            return;
        }
        
        // 确保被关注者存在
        if (userFollows.find(followeeId) == userFollows.end()) {
            userFollows[followeeId].insert(followeeId); // 默认关注自己
            userTweets[followeeId] = std::vector<Tweet>();
            return;
        }
        
        // 不能取消关注自己
        if (followerId != followeeId) {
            userFollows[followerId].erase(followeeId);
        }
    }
};

/**
 * 打印向量内容的辅助函数
 */
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 测试Twitter类的各种功能
 */
void testTwitterImplementation() {
    std::cout << "=== 测试Twitter类 ===" << std::endl;
    
    // 测试用例1: 基本功能测试
    std::cout << "测试用例1: 基本功能测试" << std::endl;
    Twitter twitter;
    twitter.postTweet(1, 5);  // 用户1发布推文5
    std::cout << "用户1的时间线: ";
    printVector(twitter.getNewsFeed(1));  // 应该返回 [5]
    
    twitter.follow(1, 2);  // 用户1关注用户2
    twitter.postTweet(2, 6);  // 用户2发布推文6
    std::cout << "用户1关注用户2后的时间线: ";
    printVector(twitter.getNewsFeed(1));  // 应该返回 [6, 5]
    
    twitter.unfollow(1, 2);  // 用户1取消关注用户2
    std::cout << "用户1取消关注用户2后的时间线: ";
    printVector(twitter.getNewsFeed(1));  // 应该返回 [5]
    
    // 测试用例2: 关注多个用户
    std::cout << "\n测试用例2: 关注多个用户" << std::endl;
    Twitter twitter2;
    twitter2.postTweet(1, 101);
    twitter2.postTweet(2, 201);
    twitter2.postTweet(3, 301);
    twitter2.follow(4, 1);  // 用户4关注用户1
    twitter2.follow(4, 2);  // 用户4关注用户2
    twitter2.follow(4, 3);  // 用户4关注用户3
    twitter2.postTweet(1, 102);
    twitter2.postTweet(2, 202);
    std::cout << "用户4的时间线: ";
    printVector(twitter2.getNewsFeed(4));  // 应该返回最近的10条推文
    
    // 测试用例3: AlternativeTwitter实现测试
    std::cout << "\n测试用例3: AlternativeTwitter实现测试" << std::endl;
    AlternativeTwitter altTwitter;
    altTwitter.postTweet(1, 5);
    altTwitter.postTweet(2, 6);
    altTwitter.follow(1, 2);
    std::cout << "AlternativeTwitter测试: ";
    printVector(altTwitter.getNewsFeed(1));  // 应该返回 [6, 5]
    
    // 测试用例4: OptimizedTwitter实现测试
    std::cout << "\n测试用例4: OptimizedTwitter实现测试" << std::endl;
    OptimizedTwitter optTwitter;
    optTwitter.postTweet(1, 5);
    optTwitter.postTweet(2, 6);
    optTwitter.follow(1, 2);
    std::cout << "OptimizedTwitter测试: ";
    printVector(optTwitter.getNewsFeed(1));  // 应该返回 [6, 5]
    
    std::cout << "\n所有测试用例执行完毕！" << std::endl;
}

int main() {
    testTwitterImplementation();
    return 0;
}