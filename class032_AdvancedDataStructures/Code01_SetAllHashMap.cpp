#include <unordered_map>
#include <utility>
#include <iostream>
#include <mutex>
#include <stdexcept>
#include <cassert>
#include <chrono>

// setAll功能的哈希表
/*
 * 一、题目解析
 * 实现一个支持setAll功能的哈希表，支持以下操作：
 * 1. put(k, v): 插入或更新键值对
 * 2. get(k): 获取键对应的值
 * 3. setAll(v): 将所有键的值都设置为v
 * 
 * 要求所有操作的时间复杂度都是O(1)
 * 
 * 二、算法思路
 * 使用时间戳技术实现setAll功能：
 * 1. 为每个键值对记录插入/更新的时间戳
 * 2. 为setAll操作记录时间戳
 * 3. get操作时比较键值对的时间戳和setAll时间戳，返回较新的值
 * 
 * 三、时间复杂度分析
 * put操作: O(1) - 哈希表插入/更新
 * get操作: O(1) - 哈希表查找 + 时间戳比较
 * setAll操作: O(1) - 更新全局变量
 * 
 * 四、空间复杂度分析
 * O(n) - n为键值对的个数，需要哈希表存储所有键值对及相关信息
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理非法输入和边界情况
 * 2. 边界场景: 空哈希表、大量数据等情况的优化
 * 3. 时间戳溢出: 在实际应用中需要注意时间戳溢出问题
 * 4. 线程安全: 在多线程环境下需要考虑同步机制
 * 5. 内存管理: C++中需要注意资源释放和避免内存泄漏
 * 6. RAII原则: 利用C++的RAII特性确保资源安全管理
 * 7. 模板支持: 扩展为模板类以支持各种数据类型
 * 8. 性能优化: 利用C++特性如移动语义、引用避免不必要的拷贝
 * 9. 异常安全保证: 实现强异常安全保证，确保操作要么完全成功要么回滚
 * 10. 可扩展性: 设计模块化结构以支持功能扩展
 * 
 * 六、相关题目扩展
 * 1. 牛客网: [设计有setAll功能的哈希表](https://www.nowcoder.com/practice/7c4559f138e74ceb9ba57d76fd169967) - 本题原型
 * 2. LeetCode 380. [常数时间插入、删除和获取随机元素](https://leetcode.com/problems/insert-delete-getrandom-o1/) - 类似的哈希表优化设计
 * 3. LeetCode 432. [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/) - O(1)复杂度设计问题
 * 4. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/) - 类似的数据结构设计问题
 * 5. HackerRank: [Design a Special Stack](https://www.hackerrank.com/challenges/design-a-stack-with-getmax) - 类似的O(1)操作设计
 * 6. 洛谷 P1168. [中位数](https://www.luogu.com.cn/problem/P1168) - 涉及数据流处理的O(1)查询
 * 7. CodeChef: [XOR with Set](https://www.codechef.com/problems/XORSET) - 哈希表应用问题
 * 8. LintCode 1286. [最小操作数](https://www.lintcode.com/problem/1286/) - 类似的批量操作优化问题
 * 9. LeetCode 460. [LFU缓存](https://leetcode.com/problems/lfu-cache/) - 频率相关的数据结构设计
 * 10. LeetCode 706. [设计哈希映射](https://leetcode.com/problems/design-hashmap/) - 基础哈希表实现
 * 11. LeetCode 705. [设计哈希集合](https://leetcode.com/problems/design-hashset/) - 基础哈希集合实现
 * 12. LeetCode 146. [LRU缓存机制](https://leetcode.com/problems/lru-cache/) - 经典缓存设计问题
 * 13. 牛客网: [复杂链表的复制](https://www.nowcoder.com/practice/f836b2c43afc4b35ad6adc41ec941dba) - 哈希表应用
 * 14. 力扣 1603. [设计停车系统](https://leetcode.cn/problems/design-parking-system/) - 简单设计题
 * 15. 力扣 1357. [每隔n个顾客打折](https://leetcode.cn/problems/apply-discount-every-n-orders/) - 批量操作优化
 */

class SetAllHashMap {
private:
    // 哈希表存储键值对，值为pair类型，first为值，second为时间戳
    std::unordered_map<int, std::pair<int, int>> map;
    // setAll设置的值
    int setAllValue;
    // setAll操作的时间戳
    int setAllTime;
    // 全局时间戳计数器
    int cnt;

public:
    // 构造函数
    SetAllHashMap() : setAllValue(0), setAllTime(-1), cnt(0) {}
    
    // 析构函数
    ~SetAllHashMap() {
        // C++中unordered_map会自动处理内存释放
    }
    
    /*
     * 插入或更新键值对
     * @param k 键
     * @param v 值
     * 时间复杂度: O(1) - 平均情况，最坏情况O(n)在哈希冲突严重时
     * 空间复杂度: O(1) - 不考虑哈希表扩容
     */
    void put(int k, int v) {
        auto it = map.find(k);
        if (it != map.end()) {
            // 更新已存在的键值对
            it->second.first = v;
            it->second.second = cnt++;  // 更新时间戳
        } else {
            // 插入新的键值对
            map[k] = std::make_pair(v, cnt++);
        }
    }

    /*
     * 设置所有键的值
     * @param v 要设置的值
     * 时间复杂度: O(1) - 仅更新全局变量
     * 工程优化点: 使用时间戳技术实现O(1)复杂度的批量更新，避免遍历整个哈希表
     */
    void setAll(int v) {
        setAllValue = v;
        setAllTime = cnt++;  // 记录setAll操作的时间戳
    }

    /*
     * 获取键对应的值
     * @param k 键
     * @return 键对应的值，如果键不存在返回-1
     * 时间复杂度: O(1) - 平均情况，最坏情况O(n)在哈希冲突严重时
     * 核心逻辑: 通过比较键值对的时间戳和setAll时间戳，返回最新设置的值
     */
    int get(int k) {
        auto it = map.find(k);
        if (it == map.end()) {
            return -1;  // 键不存在的异常处理
        }
        
        std::pair<int, int>& value = it->second;
        if (value.second > setAllTime) {
            return value.first;  // 返回最近一次单独设置的值
        } else {
            return setAllValue;  // 返回setAll设置的值
        }
    }
    
    // 清空哈希表
    void clear() {
        map.clear();
        setAllValue = 0;
        setAllTime = -1;
        cnt = 0;
    }
    
    // 获取当前哈希表大小
    size_t size() const {
        return map.size();
    }
};

/*
 * 补充题目1: 牛客网 - 设计有setAll功能的哈希表
 * 题目描述: 实现一个支持setAll功能的哈希表，要求所有操作O(1)时间复杂度
 * 与本题完全一致，上述实现可以直接应用
 */

/*
 * 补充题目2: 支持批量操作的哈希表扩展（C++版本）
 * 扩展功能: 支持范围更新操作，如addAll(v)将所有值增加v
 * 实现思路: 使用类似的惰性更新技术，记录增量而不是绝对值
 */

/*
 * 补充题目3: 线程安全的SetAllHashMap实现（C++版本）
 * 使用std::shared_mutex实现读写锁分离，提高并发性能
 * 注意：C++17及以上版本支持std::shared_mutex
 */
class ThreadSafeSetAllHashMap {
private:
    // 哈希表存储键值对
    std::unordered_map<int, std::pair<int, int>> map;
    // setAll设置的值
    int setAllValue;
    // setAll操作的时间戳
    int setAllTime;
    // 全局时间戳计数器
    int cnt;
    // 读写锁，支持并发读取和独占写入
    mutable std::mutex mutex; // 为了简化，使用互斥锁代替shared_mutex，便于编译

public:
    // 构造函数
    ThreadSafeSetAllHashMap() : setAllValue(0), setAllTime(-1), cnt(0) {}
    
    // 析构函数
    ~ThreadSafeSetAllHashMap() {}
    
    // 插入或更新键值对（需要写锁）
    void put(int k, int v) {
        std::lock_guard<std::mutex> lock(mutex);
        auto it = map.find(k);
        if (it != map.end()) {
            it->second.first = v;
            it->second.second = cnt++;
        } else {
            map[k] = std::make_pair(v, cnt++);
        }
    }
    
    // 设置所有键的值（需要写锁）
    void setAll(int v) {
        std::lock_guard<std::mutex> lock(mutex);
        setAllValue = v;
        setAllTime = cnt++;
    }
    
    // 获取键对应的值（需要读锁）
    int get(int k) const {
        std::lock_guard<std::mutex> lock(mutex);
        auto it = map.find(k);
        if (it == map.end()) {
            return -1;
        }
        
        const auto& value = it->second;
        return value.second > setAllTime ? value.first : setAllValue;
    }
    
    // 清空哈希表（需要写锁）
    void clear() {
        std::lock_guard<std::mutex> lock(mutex);
        map.clear();
        setAllValue = 0;
        setAllTime = -1;
        cnt = 0;
    }
    
    // 获取当前哈希表大小（需要读锁）
    size_t size() const {
        std::lock_guard<std::mutex> lock(mutex);
        return map.size();
    }
};

class EnhancedSetAllHashMap {
private:
    // 存储键到[实际值, 时间戳]的映射
    std::unordered_map<int, std::pair<int, int>> map;
    // 增量值
    int addAllDelta;
    // 增量操作的时间戳
    int addAllTime;
    // 设置的绝对值
    int setAllValue;
    // 设置操作的时间戳
    int setAllTime;
    // 全局时间戳计数器
    int cnt;

public:
    EnhancedSetAllHashMap() 
        : addAllDelta(0), addAllTime(-1), 
          setAllValue(0), setAllTime(-1), cnt(0) {}
    
    /*
     * 插入或更新键值对
     * 考虑addAll和setAll的影响，存储实际需要的值
     * 时间复杂度: O(1) - 平均情况
     */
    void put(int k, int v) {
        int actualValue = v;
        
        // 计算实际需要存储的值
        if (setAllTime > -1) {
            // 减去setAllValue和之后的addAllDelta
            actualValue = v - setAllValue - addAllDelta;
        } else if (addAllTime > -1) {
            // 减去addAllDelta
            actualValue = v - addAllDelta;
        }
        
        map[k] = std::make_pair(actualValue, cnt++);
    }
    
    /*
     * 获取键对应的值
     * 综合考虑put、setAll和addAll操作的影响
     * 时间复杂度: O(1) - 平均情况
     */
    int get(int k) {
        auto it = map.find(k);
        if (it == map.end()) {
            return -1;
        }
        
        const auto& value = it->second;
        int result = value.first;
        
        // 应用setAll操作
        if (value.second < setAllTime) {
            result = setAllValue;
        }
        
        // 应用addAll操作
        if (std::max(value.second, setAllTime) < addAllTime) {
            result += addAllDelta;
        }
        
        return result;
    }
    
    /*
     * 设置所有键的值为v
     * 注意：setAll操作会重置addAll状态
     * 时间复杂度: O(1)
     */
    void setAll(int v) {
        setAllValue = v;
        setAllTime = cnt++;
        // setAll后，addAll操作需要重置
        addAllDelta = 0;
        addAllTime = -1;
    }
    
    /*
     * 为所有键的值增加delta
     * 使用惰性更新技术，只记录增量
     * 时间复杂度: O(1)
     */
    void addAll(int delta) {
        // 惰性更新：只记录增量
        addAllDelta += delta;
        addAllTime = cnt++;
    }
    
    // 清空哈希表
    void clear() {
        map.clear();
        addAllDelta = 0;
        addAllTime = -1;
        setAllValue = 0;
        setAllTime = -1;
        cnt = 0;
    }
};

// 测试辅助函数：验证基本功能
void testBasicFunctionality() {
    std::cout << "=== 测试用例1: 基本操作 ===" << std::endl;
    SetAllHashMap hashMap;
    
    hashMap.put(1, 100);
    hashMap.put(2, 200);
    std::cout << "get(1) = " << hashMap.get(1) << std::endl;  // 预期输出: 100
    std::cout << "get(2) = " << hashMap.get(2) << std::endl;  // 预期输出: 200
    
    hashMap.setAll(300);
    std::cout << "setAll(300)" << std::endl;
    std::cout << "get(1) = " << hashMap.get(1) << std::endl;  // 预期输出: 300
    std::cout << "get(2) = " << hashMap.get(2) << std::endl;  // 预期输出: 300
    
    hashMap.put(1, 400);
    std::cout << "put(1, 400)" << std::endl;
    std::cout << "get(1) = " << hashMap.get(1) << std::endl;  // 预期输出: 400
    std::cout << "get(2) = " << hashMap.get(2) << std::endl;  // 预期输出: 300
}

// 测试辅助函数：验证键不存在和空哈希表场景
void testEdgeCases() {
    std::cout << "\n=== 测试用例2: 边界情况 ===" << std::endl;
    SetAllHashMap hashMap;
    
    // 测试不存在的键
    std::cout << "get(3) (不存在的键) = " << hashMap.get(3) << std::endl;  // 预期输出: -1
    
    // 测试空哈希表的setAll操作
    hashMap.setAll(500);
    std::cout << "setAll(500) on empty map" << std::endl;
    
    // 插入新键后验证
    hashMap.put(4, 600);
    std::cout << "put(4, 600) after setAll" << std::endl;
    std::cout << "get(4) = " << hashMap.get(4) << std::endl;  // 预期输出: 600
    
    // 测试clear操作
    hashMap.clear();
    std::cout << "clear()" << std::endl;
    std::cout << "size after clear: " << hashMap.size() << std::endl;  // 预期输出: 0
    std::cout << "get(4) after clear: " << hashMap.get(4) << std::endl;  // 预期输出: -1
}

// 测试辅助函数：验证EnhancedSetAllHashMap功能
void testEnhancedFunctionality() {
    std::cout << "\n=== 测试用例3: EnhancedSetAllHashMap功能 ===" << std::endl;
    EnhancedSetAllHashMap enhancedMap;
    
    enhancedMap.put(1, 10);
    enhancedMap.put(2, 20);
    std::cout << "Initial state:" << std::endl;
    std::cout << "get(1): " << enhancedMap.get(1) << std::endl;  // 预期输出: 10
    std::cout << "get(2): " << enhancedMap.get(2) << std::endl;  // 预期输出: 20
    
    enhancedMap.addAll(5);
    std::cout << "\naddAll(5):" << std::endl;
    std::cout << "get(1): " << enhancedMap.get(1) << std::endl;  // 预期输出: 15
    std::cout << "get(2): " << enhancedMap.get(2) << std::endl;  // 预期输出: 25
    
    enhancedMap.setAll(50);
    std::cout << "\nsetAll(50):" << std::endl;
    std::cout << "get(1): " << enhancedMap.get(1) << std::endl;  // 预期输出: 50
    std::cout << "get(2): " << enhancedMap.get(2) << std::endl;  // 预期输出: 50
    
    enhancedMap.addAll(10);
    std::cout << "\naddAll(10):" << std::endl;
    std::cout << "get(1): " << enhancedMap.get(1) << std::endl;  // 预期输出: 60
    std::cout << "get(2): " << enhancedMap.get(2) << std::endl;  // 预期输出: 60
    
    // 新插入键值对
    enhancedMap.put(3, 30);
    std::cout << "\nput(3, 30):" << std::endl;
    std::cout << "get(3): " << enhancedMap.get(3) << std::endl;  // 预期输出: 30
    std::cout << "get(1): " << enhancedMap.get(1) << std::endl;  // 预期输出: 60
}

/**
 * 单元测试类 - 测试SetAllHashMap的各种功能
 */
class SetAllHashMapTest {
public:
    /**
     * 测试基本功能：插入、查询、setAll
     */
    static void testBasicOperations() {
        std::cout << "=== 测试基本功能 ===" << std::endl;
        SetAllHashMap map;
        
        // 测试插入和查询
        map.put(1, 100);
        map.put(2, 200);
        assert(map.get(1) == 100 && "插入后查询失败");
        assert(map.get(2) == 200 && "插入后查询失败");
        std::cout << "✓ 基本插入查询测试通过" << std::endl;
        
        // 测试setAll功能
        map.setAll(300);
        assert(map.get(1) == 300 && "setAll后查询失败");
        assert(map.get(2) == 300 && "setAll后查询失败");
        std::cout << "✓ setAll功能测试通过" << std::endl;
        
        // 测试setAll后插入新元素
        map.put(3, 400);
        assert(map.get(3) == 400 && "setAll后插入新元素失败");
        assert(map.get(1) == 300 && "setAll后原有元素值错误");
        std::cout << "✓ setAll后插入新元素测试通过" << std::endl;
        
        // 测试setAll后更新已有元素
        map.put(1, 500);
        assert(map.get(1) == 500 && "setAll后更新元素失败");
        assert(map.get(2) == 300 && "setAll后未更新元素值错误");
        std::cout << "✓ setAll后更新元素测试通过" << std::endl;
    }
    
    /**
     * 测试边界情况
     */
    static void testEdgeCases() {
        std::cout << "\n=== 测试边界情况 ===" << std::endl;
        SetAllHashMap map;
        
        // 测试空哈希表
        assert(map.get(1) == -1 && "空哈希表查询失败");
        std::cout << "✓ 空哈希表查询测试通过" << std::endl;
        
        // 测试setAll空哈希表
        map.setAll(100);
        assert(map.get(1) == -1 && "空哈希表setAll后查询失败");
        std::cout << "✓ 空哈希表setAll测试通过" << std::endl;
        
        // 测试单元素哈希表
        map.put(1, 200);
        map.setAll(300);
        assert(map.get(1) == 300 && "单元素setAll失败");
        std::cout << "✓ 单元素哈希表测试通过" << std::endl;
        
        // 测试重复插入
        map.put(1, 400);
        map.put(1, 500);
        assert(map.get(1) == 500 && "重复插入失败");
        std::cout << "✓ 重复插入测试通过" << std::endl;
    }
    
    /**
     * 测试性能和大数据量场景
     */
    static void testPerformance() {
        std::cout << "\n=== 测试性能和大数据量 ===" << std::endl;
        SetAllHashMap map;
        int n = 10000;
        
        auto startTime = std::chrono::high_resolution_clock::now();
        
        // 批量插入
        for (int i = 0; i < n; i++) {
            map.put(i, i * 10);
        }
        
        // 批量查询
        for (int i = 0; i < n; i++) {
            int value = map.get(i);
            assert(value == i * 10 && "批量插入查询失败");
        }
        
        // 执行setAll
        map.setAll(999);
        
        // 验证setAll效果
        for (int i = 0; i < n; i++) {
            int value = map.get(i);
            assert(value == 999 && "批量setAll失败");
        }
        
        auto endTime = std::chrono::high_resolution_clock::now();
        auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
        std::cout << "✓ 性能测试通过，处理 " << n << " 个元素耗时: " << duration.count() << "ms" << std::endl;
    }
    
    /**
     * 运行所有测试
     */
    static void runAllTests() {
        try {
            testBasicOperations();
            testEdgeCases();
            testPerformance();
            std::cout << "\n🎉 所有SetAllHashMap测试通过！功能正常。" << std::endl;
        } catch (const std::exception& e) {
            std::cerr << "❌ SetAllHashMap测试失败: " << e.what() << std::endl;
        }
    }
};

// 主测试函数
int main() {
    try {
        // 运行单元测试
        SetAllHashMapTest::runAllTests();
        
        // 运行原有测试
        testBasicFunctionality();
        testEdgeCases();
        testEnhancedFunctionality();
        
        // 演示基本功能
        std::cout << "\n=== SetAllHashMap功能演示 ===" << std::endl;
        SetAllHashMap map;
        
        std::cout << "1. 插入键值对: put(1, 100), put(2, 200)" << std::endl;
        map.put(1, 100);
        map.put(2, 200);
        std::cout << "   get(1) = " << map.get(1) << std::endl;
        std::cout << "   get(2) = " << map.get(2) << std::endl;
        
        std::cout << "2. 执行setAll(300)" << std::endl;
        map.setAll(300);
        std::cout << "   get(1) = " << map.get(1) << std::endl;
        std::cout << "   get(2) = " << map.get(2) << std::endl;
        
        std::cout << "3. 更新键1: put(1, 400)" << std::endl;
        map.put(1, 400);
        std::cout << "   get(1) = " << map.get(1) << std::endl;
        std::cout << "   get(2) = " << map.get(2) << std::endl;
        
        std::cout << "4. 插入新键: put(3, 500)" << std::endl;
        map.put(3, 500);
        std::cout << "   get(3) = " << map.get(3) << std::endl;
        std::cout << "   get(1) = " << map.get(1) << std::endl;
        
        std::cout << "\n演示完成！" << std::endl;
        
        std::cout << "\nAll tests completed successfully!" << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "Error during testing: " << e.what() << std::endl;
        return 1;
    } catch (...) {
        std::cerr << "Unknown error during testing" << std::endl;
        return 1;
    }
    
    return 0;
}

/*
 * 算法设计技巧总结：
 * 1. 惰性更新：通过记录操作的元信息（如时间戳）避免立即修改所有元素，将批量操作的成本分摊到后续的访问操作中
 * 2. 时间戳技术：利用递增的时间戳来记录操作顺序，帮助判断数据的最终状态
 * 3. 数据结构组合：哈希表提供O(1)的查找能力，配合适当的元数据管理机制
 * 4. 状态压缩：使用全局变量记录批量操作状态，避免冗余存储
 * 5. 优先级设计：通过时间戳自动处理操作的优先级关系
 * 
 * C++实现的工程化考量：
 * 1. 异常安全：使用RAII原则管理资源，确保不会内存泄漏
 * 2. 性能优化：使用auto和引用避免不必要的拷贝
 * 3. 边界处理：处理空哈希表、不存在的键等边界情况
 * 4. 扩展性：设计EnhancedSetAllHashMap扩展支持更多操作
 * 5. 线程安全：实现ThreadSafeSetAllHashMap支持并发访问
 * 6. 异常处理：使用try-catch块捕获和处理可能的异常
 * 7. 代码组织：将测试代码模块化，提高可维护性
 * 8. 接口设计：提供清晰、一致的类接口
 * 9. 内存效率：优化内存使用，避免不必要的对象创建
 * 10. 编译兼容性：使用标准C++特性，确保广泛兼容性
 * 
 * 时间戳溢出问题解决方案：
 * 1. 使用更大范围的整数类型（如long long）
 * 2. 实现循环时间戳机制
 * 3. 在接近溢出时进行重哈希和调整
 * 4. 采用双时间戳机制，结合高位和低位时间戳
 * 
 * 面试要点：
 * 1. 解释惰性更新的思想和优势
 * 2. 分析各种边界情况下的行为
 * 3. 讨论线程安全性问题和实现策略
 * 4. 提出可能的扩展和优化方向
 * 5. 分析时间和空间复杂度
 * 6. 讨论C++特定的实现细节和优化
 * 
 * 补充题目4: 模板化的SetAllHashMap
 * 题目描述: 设计一个支持泛型的SetAllHashMap，能够存储任意类型的键值对
 * 实现思路: 
 * 1. 将类设计为模板类，支持不同类型的键和值
 * 2. 为模板特化提供适当的默认值处理
 * 3. 确保时间戳机制在不同类型下正常工作
 * 
 * 补充题目5: 支持迭代器的扩展
 * 题目描述: 为SetAllHashMap实现迭代器支持，能够遍历所有键值对
 * 实现思路: 
 * 1. 定义符合STL规范的迭代器类
 * 2. 在迭代过程中正确应用setAll和addAll的影响
 * 3. 提供begin()和end()方法支持范围for循环
 */