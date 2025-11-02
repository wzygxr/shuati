#include <iostream>
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <queue>
#include <memory>

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

// 推文结构体
struct Tweet {
    int tweetId;
    int time;
    Tweet* next; // 链表结构
    
    Tweet(int id, int t) : tweetId(id), time(t), next(nullptr) {}
};

class Twitter {
private:
    // 存储用户发布的推文
    std::unordered_map<int, Tweet*> userTweets;
    
    // 存储用户的关注关系
    std::unordered_map<int, std::unordered_set<int>> userFollows;
    
    // 全局时间戳计数器
    int timeCounter;
    
    // 每次获取的最大推文数量
    const int MAX_TWEETS = 10;
    
    // 确保用户存在
    void ensureUserExists(int userId) {
        if (userTweets.find(userId) == userTweets.end()) {
            userTweets[userId] = nullptr;
            userFollows[userId] = std::unordered_set<int>();
        }
    }
    
public:
    /**
     * 初始化Twitter对象
     */
    Twitter() : timeCounter(0) {
    }
    
    /**
     * 析构函数，释放内存
     */
    ~Twitter() {
        // 释放所有推文的内存
        for (auto& pair : userTweets) {
            Tweet* current = pair.second;
            while (current != nullptr) {
                Tweet* next = current->next;
                delete current;
                current = next;
            }
        }
    }
    
    /**
     * 发布一条新推文
     * @param userId 用户ID
     * @param tweetId 推文ID
     */
    void postTweet(int userId, int tweetId) {
        // 确保用户存在
        ensureUserExists(userId);
        
        // 创建新推文并添加到链表头部（最新的在前面）
        Tweet* newTweet = new Tweet(tweetId, timeCounter++);
        newTweet->next = userTweets[userId];
        userTweets[userId] = newTweet;
    }
    
    /**
     * 获取用户及其关注者的最近10条推文
     * @param userId 用户ID
     * @return 按时间倒序排列的推文ID列表
     */
    std::vector<int> getNewsFeed(int userId) {
        std::vector<int> result;
        
        // 确保用户存在
        ensureUserExists(userId);
        
        // 使用优先队列合并多个有序链表
        // 优先队列按照推文时间降序排列（最大堆）
        // 存储的是指向Tweet的指针和该推文所属的用户下一条推文
        using TweetTuple = std::tuple<int, Tweet*>; // (time, tweet)
        auto cmp = [](const TweetTuple& a, const TweetTuple& b) {
            return std::get<0>(a) < std::get<0>(b); // 小顶堆，取最大元素时使用
        };
        std::priority_queue<TweetTuple, std::vector<TweetTuple>, decltype(cmp)> maxHeap(cmp);
        
        // 添加用户自己的最新推文
        if (userTweets[userId] != nullptr) {
            maxHeap.push({userTweets[userId]->time, userTweets[userId]});
        }
        
        // 添加用户关注的人的最新推文
        for (int followeeId : userFollows[userId]) {
            if (userTweets.find(followeeId) != userTweets.end() && userTweets[followeeId] != nullptr) {
                maxHeap.push({userTweets[followeeId]->time, userTweets[followeeId]});
            }
        }
        
        // 取出最多10条最新推文
        int count = 0;
        while (!maxHeap.empty() && count < MAX_TWEETS) {
            auto [time, current] = maxHeap.top();
            maxHeap.pop();
            
            result.push_back(current->tweetId);
            count++;
            
            // 如果该用户还有更早的推文，将其下一条添加到优先队列
            if (current->next != nullptr) {
                maxHeap.push({current->next->time, current->next});
            }
        }
        
        return result;
    }
    
    /**
     * 用户关注另一个用户
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    void follow(int followerId, int followeeId) {
        // 不能关注自己
        if (followerId == followeeId) {
            return;
        }
        
        // 确保用户存在
        ensureUserExists(followerId);
        ensureUserExists(followeeId);
        
        // 添加关注关系
        userFollows[followerId].insert(followeeId);
    }
    
    /**
     * 用户取消关注另一个用户
     * @param followerId 关注者ID
     * @param followeeId 被关注者ID
     */
    void unfollow(int followerId, int followeeId) {
        // 确保用户存在
        ensureUserExists(followerId);
        
        // 移除关注关系
        userFollows[followerId].erase(followeeId);
    }
};

/**
 * 打印向量辅助函数
 */
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); i++) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 主函数，用于测试
 */
int main() {
    Twitter twitter;
    
    // 测试用例1：基本功能测试
    twitter.postTweet(1, 5);  // 用户1发布推文5
    std::vector<int> feed1 = twitter.getNewsFeed(1);  // 应该返回 [5]
    std::cout << "测试用例1结果：";
    printVector(feed1);
    
    // 测试用例2：关注和取消关注
    twitter.follow(1, 2);  // 用户1关注用户2
    twitter.postTweet(2, 6);  // 用户2发布推文6
    std::vector<int> feed2 = twitter.getNewsFeed(1);  // 应该返回 [6, 5]
    std::cout << "测试用例2结果：";
    printVector(feed2);
    
    twitter.unfollow(1, 2);  // 用户1取消关注用户2
    std::vector<int> feed3 = twitter.getNewsFeed(1);  // 应该返回 [5]
    std::cout << "测试用例3结果：";
    printVector(feed3);
    
    // 测试用例3：多个用户发布多条推文
    twitter.postTweet(1, 7);
    twitter.postTweet(1, 8);
    twitter.follow(1, 3);
    twitter.postTweet(3, 9);
    twitter.postTweet(3, 10);
    std::vector<int> feed4 = twitter.getNewsFeed(1);  // 应该返回 [10, 9, 8, 7, 5]
    std::cout << "测试用例4结果：";
    printVector(feed4);
    
    // 测试用例4：边界情况 - 获取超过10条推文
    twitter.postTweet(1, 11);
    twitter.postTweet(1, 12);
    twitter.postTweet(1, 13);
    twitter.postTweet(1, 14);
    twitter.postTweet(1, 15);
    twitter.postTweet(1, 16);
    twitter.postTweet(1, 17);
    std::vector<int> feed5 = twitter.getNewsFeed(1);  // 应该只返回最近的10条
    std::cout << "测试用例5结果：";
    printVector(feed5);
    std::cout << "测试用例5结果长度：" << feed5.size() << std::endl;  // 应该是10
    
    return 0;
}

/*
工程化考量：
1. 内存管理：
   - 使用析构函数释放所有动态分配的内存，避免内存泄漏
   - 在实际应用中可以考虑使用智能指针（如std::unique_ptr）

2. 异常处理：
   - C++中可以使用try-catch块处理异常
   - 对于可能的内存分配失败，可以添加异常处理

3. 线程安全：
   - 在多线程环境中，需要添加互斥锁（mutex）保证线程安全
   - 可以使用读写锁优化并发性能

4. 性能优化：
   - 使用哈希表提高查找效率
   - 使用优先队列高效合并多个有序列表
   - 提前预分配向量空间减少动态扩容

5. 代码可维护性：
   - 添加辅助函数如ensureUserExists提高代码复用性
   - 使用typedef或using定义复杂类型
   - 添加详细的注释

6. 边界条件处理：
   - 处理用户不存在的情况
   - 处理用户不能关注自己的情况
   - 确保获取的推文数量不超过限制

7. C++特性应用：
   - 使用结构化绑定（C++17）简化代码
   - 使用lambda表达式定义自定义比较函数
   - 使用移动语义优化性能

8. 调试技巧：
   - 可以使用GDB或VS调试器设置断点
   - 添加日志输出关键变量
   - 使用断言验证中间结果

9. 性能退化排查：
   - 当数据规模增大时，可能需要调整数据结构
   - 考虑使用缓存机制优化热点用户的查询

10. 扩展性：
    - 可以轻松扩展添加新功能
    - 可以替换为更高效的存储后端
*/