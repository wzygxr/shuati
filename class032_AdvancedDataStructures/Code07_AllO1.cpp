#include <unordered_map>
#include <unordered_set>
#include <string>
#include <climits>
#include <iostream>

// 全O(1)的数据结构
/*
 * 一、题目解析
 * 设计一个数据结构支持以下操作，所有操作的时间复杂度都为O(1)：
 * 1. inc(key): 将key的计数增加1，如果key不存在则插入计数为1的key
 * 2. dec(key): 将key的计数减少1，如果计数变为0则删除key
 * 3. getMaxKey(): 返回计数最大的任意一个key，如果不存在返回空字符串
 * 4. getMinKey(): 返回计数最小的任意一个key，如果不存在返回空字符串
 * 
 * 二、算法思路
 * 使用双向链表+哈希表的组合数据结构：
 * 1. 双向链表节点存储计数值和拥有该计数值的所有key集合
 * 2. 哈希表存储key到链表节点的映射
 * 3. 维护头尾哨兵节点简化边界处理
 * 4. 保证链表按计数值单调递增排列
 * 
 * 三、时间复杂度分析
 * 所有操作: O(1) - 哈希表操作和链表节点操作都是O(1)
 * 
 * 四、空间复杂度分析
 * O(n) - n为不同key的个数，需要链表节点和哈希表存储相关信息
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空数据结构的getMaxKey和getMinKey操作
 * 2. 边界场景: 空数据结构、单元素数据结构等
 * 3. 内存管理: C++需要手动管理内存
 * 
 * 六、相关题目扩展
 * 1. LeetCode 432. 全O(1)的数据结构 (本题)
 * 2. LeetCode 146. LRU缓存
 * 3. 牛客网相关设计题目
 */

class AllOne {
private:
    /*
     * 链表节点类
     * 存储计数值和拥有该计数值的所有key集合
     */
    struct Bucket {
        std::unordered_set<std::string> set;
        int cnt;
        Bucket* last;
        Bucket* next;

        Bucket(const std::string& s, int c) : cnt(c), last(nullptr), next(nullptr) {
            set.insert(s);
        }
    };

    /*
     * 在cur节点后插入pos节点
     */
    void insert(Bucket* cur, Bucket* pos) {
        cur->next->last = pos;
        pos->next = cur->next;
        cur->next = pos;
        pos->last = cur;
    }

    /*
     * 移除cur节点
     */
    void remove(Bucket* cur) {
        cur->last->next = cur->next;
        cur->next->last = cur->last;
        delete cur;
    }

    // 头节点（计数值为0的哨兵节点）
    Bucket* head;

    // 尾节点（计数值为无穷大的哨兵节点）
    Bucket* tail;

    // 哈希表存储key到链表节点的映射
    std::unordered_map<std::string, Bucket*> map;

public:
    // 构造函数
    AllOne() {
        head = new Bucket("", 0);
        tail = new Bucket("", INT_MAX);
        head->next = tail;
        tail->last = head;
    }
    
    // 析构函数
    ~AllOne() {
        Bucket* cur = head;
        while (cur != nullptr) {
            Bucket* next = cur->next;
            delete cur;
            cur = next;
        }
    }

    /*
     * 增加key的计数
     * @param key 要增加计数的key
     * 时间复杂度: O(1)
     */
    void inc(std::string key) {
        if (map.find(key) == map.end()) {
            // 如果key不存在
            if (head->next->cnt == 1) {
                // 如果计数值为1的节点已存在，直接添加key
                map[key] = head->next;
                head->next->set.insert(key);
            } else {
                // 否则创建新节点并插入链表
                Bucket* newBucket = new Bucket(key, 1);
                map[key] = newBucket;
                insert(head, newBucket);
            }
        } else {
            // 如果key已存在
            Bucket* bucket = map[key];
            if (bucket->next->cnt == bucket->cnt + 1) {
                // 如果计数值+1的节点已存在，直接添加key
                map[key] = bucket->next;
                bucket->next->set.insert(key);
            } else {
                // 否则创建新节点并插入链表
                Bucket* newBucket = new Bucket(key, bucket->cnt + 1);
                map[key] = newBucket;
                insert(bucket, newBucket);
            }
            // 从原节点中移除key
            bucket->set.erase(key);
            // 如果原节点的key集合为空，则删除该节点
            if (bucket->set.empty()) {
                remove(bucket);
            }
        }
    }

    /*
     * 减少key的计数
     * @param key 要减少计数的key
     * 时间复杂度: O(1)
     */
    void dec(std::string key) {
        // 获取key对应的节点
        Bucket* bucket = map[key];
        if (bucket->cnt == 1) {
            // 如果计数值为1，直接删除key
            map.erase(key);
        } else {
            // 如果计数值大于1
            if (bucket->last->cnt == bucket->cnt - 1) {
                // 如果计数值-1的节点已存在，直接添加key
                map[key] = bucket->last;
                bucket->last->set.insert(key);
            } else {
                // 否则创建新节点并插入链表
                Bucket* newBucket = new Bucket(key, bucket->cnt - 1);
                map[key] = newBucket;
                insert(bucket->last, newBucket);
            }
        }
        // 从原节点中移除key
        bucket->set.erase(key);
        // 如果原节点的key集合为空，则删除该节点
        if (bucket->set.empty()) {
            remove(bucket);
        }
    }

    /*
     * 获取计数最大的key
     * @return 计数最大的key，如果不存在返回空字符串
     * 时间复杂度: O(1)
     */
    std::string getMaxKey() {
        // 如果链表为空，返回空字符串
        if (tail->last == head) {
            return "";
        }
        // 返回计数最大的节点中的任意一个key
        return *(tail->last->set.begin());
    }

    /*
     * 获取计数最小的key
     * @return 计数最小的key，如果不存在返回空字符串
     * 时间复杂度: O(1)
     */
    std::string getMinKey() {
        // 如果链表为空，返回空字符串
        if (head->next == tail) {
            return "";
        }
        // 返回计数最小的节点中的任意一个key
        return *(head->next->set.begin());
    }
};

// 测试代码
int main() {
    AllOne allOne;
    
    // 简单测试
    allOne.inc("hello");
    allOne.inc("world");
    allOne.inc("hello");
    std::cout << "getMaxKey(): " << allOne.getMaxKey() << std::endl;  // hello
    std::cout << "getMinKey(): " << allOne.getMinKey() << std::endl;  // world
    allOne.inc("world");
    allOne.inc("world");
    std::cout << "getMaxKey(): " << allOne.getMaxKey() << std::endl;  // world
    allOne.dec("world");
    std::cout << "getMinKey(): " << allOne.getMinKey() << std::endl;  // hello or world
    
    return 0;
}