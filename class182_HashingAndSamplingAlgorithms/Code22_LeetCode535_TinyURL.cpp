#include <iostream>
#include <string>
#include <unordered_map>
#include <vector>
#include <mutex>
#include <atomic>
#include <algorithm>
#include <random>

using namespace std;

/**
 * LeetCode 535. TinyURL 的加密与解密
 * 题目链接：https://leetcode.com/problems/encode-and-decode-tinyurl/
 * 
 * 题目描述：
 * TinyURL是一种URL简化服务。比如当你输入一个URL https://leetcode.com/problems/design-tinyurl时，
 * 它将返回一个简化的URL http://tinyurl.com/4e9iAk。
 * 要求设计一个TinyURL系统，包含加密和解密两个功能：
 * - 加密：将长URL转换为短URL
 * - 解密：将短URL转换回长URL
 * 
 * 算法思路：
 * 1. 使用哈希表存储长URL到短URL的映射关系
 * 2. 使用自增ID或哈希值生成唯一的短URL标识符
 * 3. 将标识符编码为短字符串（如62进制）
 * 4. 使用前缀"http://tinyurl.com/"构成完整短URL
 * 
 * 时间复杂度：
 * - encode: O(1) 平均情况
 * - decode: O(1) 平均情况
 * 
 * 空间复杂度：O(n)，其中n是存储的URL数量
 */

class Solution {
private:
    static const string CHARSET;
    static const int BASE;
    static const string PREFIX;
    
    unordered_map<string, string> shortToLongMap;
    unordered_map<string, string> longToShortMap;
    atomic<long long> idGenerator;
    mutex mtx;
    
    // 将ID转换为62进制字符串
    string idToShortKey(long long id) {
        if (id == 0) return string(1, CHARSET[0]);
        
        string shortKey;
        while (id > 0) {
            shortKey += CHARSET[id % BASE];
            id /= BASE;
        }
        reverse(shortKey.begin(), shortKey.end());
        return shortKey;
    }
    
    // 将62进制字符串转换为ID
    long long shortKeyToId(const string& shortKey) {
        long long id = 0;
        for (char c : shortKey) {
            id = id * BASE + CHARSET.find(c);
        }
        return id;
    }

public:
    Solution() : idGenerator(0) {}
    
    // 加密：将长URL编码为短URL
    string encode(string longUrl) {
        lock_guard<mutex> lock(mtx);
        
        // 检查是否已经为该长URL生成过短URL
        if (longToShortMap.find(longUrl) != longToShortMap.end()) {
            return longToShortMap[longUrl];
        }
        
        // 生成新的唯一ID
        long long id = idGenerator.fetch_add(1) + 1;
        
        // 将ID转换为62进制字符串
        string shortKey = idToShortKey(id);
        
        // 构造短URL
        string shortUrl = PREFIX + shortKey;
        
        // 存储映射关系
        shortToLongMap[shortKey] = longUrl;
        longToShortMap[longUrl] = shortUrl;
        
        return shortUrl;
    }
    
    // 解密：将短URL解码为长URL
    string decode(string shortUrl) {
        lock_guard<mutex> lock(mtx);
        
        // 提取短键
        string shortKey = shortUrl.substr(PREFIX.length());
        
        // 查找对应的长URL
        auto it = shortToLongMap.find(shortKey);
        return (it != shortToLongMap.end()) ? it->second : "";
    }
    
    // 获取系统统计信息
    unordered_map<string, long long> getStatistics() {
        lock_guard<mutex> lock(mtx);
        unordered_map<string, long long> stats;
        stats["urlCount"] = shortToLongMap.size();
        stats["nextId"] = idGenerator.load();
        return stats;
    }
};

// 静态成员初始化
const string Solution::CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
const int Solution::BASE = 62;
const string Solution::PREFIX = "http://tinyurl.com/";

/**
 * 优化版本：使用哈希值作为ID
 */
class SolutionOptimized {
private:
    static const string CHARSET;
    static const int BASE;
    static const string PREFIX;
    
    unordered_map<string, string> shortToLongMap;
    unordered_map<string, string> longToShortMap;
    mutex mtx;
    
    // 将哈希值转换为短键
    string hashToShortKey(size_t hash) {
        string shortKey;
        size_t absHash = hash; // size_t is always non-negative
        
        if (absHash == 0) {
            shortKey += CHARSET[0];
        } else {
            while (absHash > 0) {
                shortKey += CHARSET[absHash % BASE];
                absHash /= BASE;
            }
        }
        
        // 确保至少有6个字符
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, BASE - 1);
        
        while (shortKey.length() < 6) {
            shortKey += CHARSET[dis(gen)];
        }
        
        reverse(shortKey.begin(), shortKey.end());
        return shortKey;
    }

public:
    string encode(string longUrl) {
        lock_guard<mutex> lock(mtx);
        
        if (longToShortMap.find(longUrl) != longToShortMap.end()) {
            return longToShortMap[longUrl];
        }
        
        // 使用URL的哈希值作为ID
        size_t hash = hash<string>{}(longUrl);
        string shortKey = hashToShortKey(hash);
        
        // 处理哈希冲突
        while (shortToLongMap.find(shortKey) != shortToLongMap.end() && 
               shortToLongMap[shortKey] != longUrl) {
            // 如果发生冲突，添加随机字符
            random_device rd;
            mt19937 gen(rd());
            uniform_int_distribution<> dis(0, BASE - 1);
            shortKey += CHARSET[dis(gen)];
        }
        
        string shortUrl = PREFIX + shortKey;
        shortToLongMap[shortKey] = longUrl;
        longToShortMap[longUrl] = shortUrl;
        
        return shortUrl;
    }
    
    string decode(string shortUrl) {
        lock_guard<mutex> lock(mtx);
        
        string shortKey = shortUrl.substr(PREFIX.length());
        auto it = shortToLongMap.find(shortKey);
        return (it != shortToLongMap.end()) ? it->second : "";
    }
};

const string SolutionOptimized::CHARSET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
const int SolutionOptimized::BASE = 62;
const string SolutionOptimized::PREFIX = "http://tinyurl.com/";

/**
 * 测试函数
 */
void testBasicVersion() {
    cout << "--- 基础版本测试 ---" << endl;
    Solution codec;
    
    // 测试基本功能
    string url1 = "https://leetcode.com/problems/design-tinyurl";
    string shortUrl1 = codec.encode(url1);
    string decodedUrl1 = codec.decode(shortUrl1);
    
    cout << "原始URL: " << url1 << endl;
    cout << "短URL: " << shortUrl1 << endl;
    cout << "解码URL: " << decodedUrl1 << endl;
    cout << "编码解码一致性: " << (url1 == decodedUrl1 ? "true" : "false") << endl;
    
    // 测试重复URL
    string shortUrl1Again = codec.encode(url1);
    cout << "重复编码一致性: " << (shortUrl1 == shortUrl1Again ? "true" : "false") << endl;
    
    // 测试不同URL
    string url2 = "https://www.google.com";
    string shortUrl2 = codec.encode(url2);
    string decodedUrl2 = codec.decode(shortUrl2);
    
    cout << "URL2原始: " << url2 << endl;
    cout << "URL2短URL: " << shortUrl2 << endl;
    cout << "URL2解码: " << decodedUrl2 << endl;
    cout << "URL2一致性: " << (url2 == decodedUrl2 ? "true" : "false") << endl;
    
    // 统计信息
    auto stats = codec.getStatistics();
    cout << "系统统计: URL数量=" << stats["urlCount"] << ", 下一个ID=" << stats["nextId"] << endl;
    cout << endl;
}

void testOptimizedVersion() {
    cout << "--- 优化版本测试 ---" << endl;
    SolutionOptimized codec;
    
    // 测试基本功能
    string url1 = "https://leetcode.com/problems/design-tinyurl";
    string shortUrl1 = codec.encode(url1);
    string decodedUrl1 = codec.decode(shortUrl1);
    
    cout << "原始URL: " << url1 << endl;
    cout << "短URL: " << shortUrl1 << endl;
    cout << "解码URL: " << decodedUrl1 << endl;
    cout << "编码解码一致性: " << (url1 == decodedUrl1 ? "true" : "false") << endl;
    
    // 测试重复URL
    string shortUrl1Again = codec.encode(url1);
    cout << "重复编码一致性: " << (shortUrl1 == shortUrl1Again ? "true" : "false") << endl;
    cout << endl;
}

int main() {
    cout << "=== 测试 LeetCode 535. TinyURL 的加密与解密 ===" << endl;
    
    // 基础版本测试
    testBasicVersion();
    
    // 优化版本测试
    testOptimizedVersion();
    
    return 0;
}