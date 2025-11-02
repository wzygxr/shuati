#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <random>
#include <stdexcept>
#include <iostream>

// 插入、删除和获取随机元素O(1)时间且允许有重复数字的结构
/*
 * 一、题目解析
 * 设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构（允许重复元素）：
 * 1. insert(val): 将一个元素val插入到集合中，返回true
 * 2. remove(val): 如果元素val存在，则从中删除一个实例，返回true，否则返回false
 * 3. getRandom: 随机返回集合中的一个元素，每个元素被返回的概率与其在集合中的数量成线性关系
 * 
 * 二、算法思路
 * 与不允许重复的版本相比，主要变化在于需要处理重复元素：
 * 1. 使用数组(vector)存储所有元素，实现O(1)时间复杂度的随机访问
 * 2. 使用哈希表(unordered_map)存储元素值到其在数组中索引集合的映射
 * 3. 插入操作：在数组末尾添加元素，并在哈希表中记录其索引
 * 4. 删除操作：将要删除的元素与数组末尾元素交换，然后删除末尾元素，更新哈希表
 * 5. 随机获取：使用随机数生成器随机生成索引，访问数组中对应元素
 * 
 * 三、时间复杂度分析
 * insert操作: O(1) - 数组末尾插入 + 哈希表更新
 * remove操作: O(1) - 哈希表查找 + 数组元素交换 + 数组末尾删除 + 哈希表更新
 * getRandom操作: O(1) - 随机索引生成 + 数组访问
 * 
 * 四、空间复杂度分析
 * O(n) - n为集合中元素个数，需要数组和哈希表分别存储元素和索引映射
 * 
 * 五、工程化考量
 * 1. 异常处理: 处理空集合的getRandom操作
 * 2. 边界场景: 空集合、单元素集合等
 * 3. 随机性: 确保getRandom方法能真正按概率返回每个元素
 * 4. 内存管理: C++需要手动管理内存
 * 
 * 六、相关题目扩展
 * 1. LeetCode 381. 常数时间插入、删除和获取随机元素-允许重复 (本题)
 * 2. LeetCode 380. 常数时间插入、删除和获取随机元素
 * 3. 牛客网相关题目
 */

class RandomizedCollection {
private:
    // 哈希表存储元素值到其在数组中索引集合的映射
    std::unordered_map<int, std::unordered_set<int> > map;
    
    // 数组存储所有元素值
    std::vector<int> arr;
    
    // 随机数生成器
    std::random_device rd;
    std::mt19937 gen;

public:
    // 构造函数
    RandomizedCollection() : gen(rd()) {}
    
    /*
     * 插入元素
     * @param val 要插入的元素
     * @return 总是返回true
     * 时间复杂度: O(1)
     */
    bool insert(int val) {
        // 在数组末尾添加元素
        arr.push_back(val);
        // 获取或创建该元素值对应的索引集合
        std::unordered_set<int>& set = map[val];
        // 将新索引添加到集合中
        set.insert(arr.size() - 1);
        // 当且仅当该元素第一次插入时返回true
        return set.size() == 1;
    }
    
    /*
     * 删除元素
     * @param val 要删除的元素
     * @return 如果元素存在则删除并返回true，否则返回false
     * 时间复杂度: O(1)
     */
    bool remove(int val) {
        // 检查元素是否存在
        if (map.find(val) == map.end()) {
            return false;
        }
        // 获取该元素值对应的索引集合
        std::unordered_set<int>& valSet = map[val];
        // 获取其中一个索引（任意一个）
        int valAnyIndex = *(valSet.begin());
        // 获取数组末尾元素的值
        int endValue = arr.back();
        // 如果要删除的元素就是末尾元素
        if (val == endValue) {
            // 直接从索引集合中删除该索引
            valSet.erase(arr.size() - 1);
        } else {
            // 获取末尾元素值对应的索引集合
            std::unordered_set<int>& endValueSet = map[endValue];
            // 将末尾元素的索引更新为要删除元素的索引
            endValueSet.insert(valAnyIndex);
            // 更新数组中要删除元素位置的值为末尾元素值
            arr[valAnyIndex] = endValue;
            // 从末尾元素的索引集合中删除原末尾索引
            endValueSet.erase(arr.size() - 1);
            // 从要删除元素的索引集合中删除该索引
            valSet.erase(valAnyIndex);
        }
        // 删除数组末尾元素
        arr.pop_back();
        // 如果要删除元素的索引集合为空，则从哈希表中删除该元素
        if (valSet.empty()) {
            map.erase(val);
        }
        return true;
    }
    
    /*
     * 随机获取元素
     * @return 随机返回集合中的一个元素
     * 时间复杂度: O(1)
     */
    int getRandom() {
        // 检查集合是否为空
        if (arr.empty()) {
            throw std::runtime_error("集合为空，无法获取随机元素");
        }
        // 随机返回数组中的一个元素
        std::uniform_int_distribution<> dis(0, arr.size() - 1);
        return arr[dis(gen)];
    }
};

// 测试代码
int main() {
    RandomizedCollection collection;
    
    // 简单测试
    std::cout << std::boolalpha;
    std::cout << "insert(1): " << collection.insert(1) << std::endl;  // true
    std::cout << "insert(1): " << collection.insert(1) << std::endl;  // false
    std::cout << "insert(2): " << collection.insert(2) << std::endl;  // true
    std::cout << "remove(1): " << collection.remove(1) << std::endl;  // true
    std::cout << "getRandom: " << collection.getRandom() << std::endl; // 1 or 2
    
    return 0;
}