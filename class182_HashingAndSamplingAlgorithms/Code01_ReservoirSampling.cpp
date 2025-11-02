#include <iostream>
#include <vector>
#include <random>
#include <ctime>
#include <unordered_map>
#include <unordered_set>
#include <string>
#include <fstream>
#include <chrono>

/**
 * 基础的蓄水池采样类
 * 实现标准的蓄水池采样算法，用于从n个元素中随机选择m个元素
 */
class Pool {
private:
    std::vector<int> reservoir; // 蓄水池
    int size; // 池子大小
    int count; // 当前处理的元素总数
    std::mt19937 rng; // 随机数生成器

public:
    Pool(int size) : size(size), count(0), rng(std::time(nullptr)) {
        reservoir.reserve(size);
    }

    /**
     * 向池子中添加一个元素
     * 时间复杂度：O(1) 平均
     */
    void enter(int num) {
        count++;
        // 前size个元素直接入池
        if (count <= size) {
            reservoir.push_back(num);
        } else {
            // 生成1到count的随机数
            std::uniform_int_distribution<int> dist(1, count);
            int random = dist(rng);
            // 以size/count的概率替换池中元素
            if (random <= size) {
                reservoir[random - 1] = num;
            }
        }
    }

    /**
     * 获取当前池子中的元素
     */
    std::vector<int> get_bag() {
        return reservoir;
    }
};

// 链表节点定义
struct ListNode {
    int val;
    ListNode* next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode* next) : val(x), next(next) {}
};

/**
 * LeetCode 382. 链表随机节点
 * 题目描述：给定一个单链表，随机选择链表的一个节点，并返回相应的节点值。每个节点被选中的概率一样。
 * 
 * 解题思路：使用蓄水池采样算法，k=1的情况
 * 1. 保存第一个节点的值
 * 2. 遍历后续节点，对于第i个节点，以1/i的概率决定是否替换结果
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */
class SolutionLinkedList {
private:
    ListNode* head;
    std::mt19937 rng; // 随机数生成器
    
public:
    SolutionLinkedList(ListNode* head) : head(head), rng(std::time(nullptr)) {}
    
    int getRandom() {
        // 蓄水池采样 k=1
        ListNode* current = head;
        if (!current) {
            throw std::runtime_error("链表为空");
        }
        
        int result = current->val;
        int count = 1;
        
        while (current != nullptr) {
            // 以 1/count 的概率选择当前节点
            std::uniform_real_distribution<double> dist(0.0, 1.0);
            if (dist(rng) < 1.0 / count) {
                result = current->val;
            }
            count++;
            current = current->next;
        }
        
        return result;
    }
};

/**
 * LeetCode 398. 随机数索引
 * 题目描述：给定一个可能含有重复元素的整数数组，随机输出给定目标数字的索引。
 * 
 * 解题思路：使用蓄水池采样算法
 * 1. 遍历数组，找到所有等于target的元素
 * 2. 对于第k个等于target的元素，以1/k的概率决定是否选择它作为结果
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 */
class SolutionRandomPickIndex {
private:
    std::vector<int> nums;
    std::mt19937 rng; // 随机数生成器
    
public:
    SolutionRandomPickIndex(std::vector<int>& nums) : nums(nums), rng(std::time(nullptr)) {}
    
    int pick(int target) {
        int result = -1;
        int count = 0;
        
        for (int i = 0; i < nums.size(); i++) {
            if (nums[i] == target) {
                count++;
                // 以 1/count 的概率选择当前索引
                std::uniform_real_distribution<double> dist(0.0, 1.0);
                if (dist(rng) < 1.0 / count) {
                    result = i;
                }
            }
        }
        
        return result;
    }
};

// 测试函数
void testLinkedListRandomNode() {
    std::cout << "=== LeetCode 382. 链表随机节点测试 ===" << std::endl;
    // 构造链表 1->2->3->4->5
    ListNode* head = new ListNode(1);
    head->next = new ListNode(2);
    head->next->next = new ListNode(3);
    head->next->next->next = new ListNode(4);
    head->next->next->next->next = new ListNode(5);
    
    SolutionLinkedList solution(head);
    std::cout << "随机选择10次链表节点:" << std::endl;
    for (int i = 0; i < 10; i++) {
        std::cout << "选中节点值: " << solution.getRandom() << std::endl;
    }
    
    // 清理内存
    ListNode* current = head;
    while (current != nullptr) {
        ListNode* temp = current;
        current = current->next;
        delete temp;
    }
}

void testRandomPickIndex() {
    std::cout << "\n=== LeetCode 398. 随机数索引测试 ===" << std::endl;
    std::vector<int> nums = {1, 2, 3, 3, 3};
    SolutionRandomPickIndex solution(nums);
    std::cout << "随机选择目标数字3的索引10次:" << std::endl;
    for (int i = 0; i < 10; i++) {
        std::cout << "选中索引: " << solution.pick(3) << std::endl;
    }
}

/**
 * LeetCode 710. 黑名单中的随机数
 * 
 * 题目描述：给定一个包含 [0，n) 中不重复整数的黑名单 blacklist ，
 * 写一个函数，从 [0, n - 1] 范围内的任意整数中选取一个不在黑名单 blacklist 中的随机整数。
 * 要求每个有效整数被选中的概率相等。
 * 
 * 解题思路：将黑名单映射到白名单的末尾
 * 时间复杂度：O(B) 初始化，O(1) 每次查询，其中 B 是黑名单的大小
 * 空间复杂度：O(B)
 */
class SolutionBlacklistRandom {
private:
    int size; // 白名单的大小
    std::unordered_map<int, int> mapping; // 黑名单映射
    std::mt19937 rng; // 随机数生成器

public:
    SolutionBlacklistRandom(int n, const std::vector<int>& blacklist) : 
        rng(std::time(nullptr)) {
        size = n - blacklist.size();
        
        // 将黑名单中的元素添加到集合中
        std::unordered_set<int> black_set(blacklist.begin(), blacklist.end());
        
        // 映射黑名单中的元素到白名单末尾的可用元素
        int last = n - 1;
        for (int b : blacklist) {
            // 如果b已经在末尾区域，不需要映射
            if (b >= size) {
                continue;
            }
            // 找到末尾区域的白名单元素
            while (black_set.count(last)) {
                last--;
            }
            mapping[b] = last;
            last--;
        }
    }

    int pick() {
        std::uniform_int_distribution<int> dist(0, size - 1);
        int index = dist(rng);
        // 如果索引在映射中，返回映射的值
        auto it = mapping.find(index);
        return (it != mapping.end()) ? it->second : index;
    }
};

/**
 * 扩展题目：从大文件中随机选择k行
 * 
 * 问题描述：给定一个非常大的文件，无法完全加载到内存，如何随机选择k行？
 * 
 * 解题思路：使用标准的蓄水池采样算法
 * 时间复杂度：O(n)，其中n是文件的行数
 * 空间复杂度：O(k)
 */
class FileLineSampler {
private:
    std::mt19937 rng; // 随机数生成器

public:
    FileLineSampler() : rng(std::time(nullptr)) {}

    std::vector<std::string> sample_lines(const std::string& file_path, int k) {
        std::vector<std::string> reservoir;
        std::ifstream file(file_path);
        
        if (!file.is_open()) {
            std::cerr << "文件 " << file_path << " 不存在" << std::endl;
            return reservoir;
        }
        
        int i = 0;
        std::string line;
        
        // 先填充前k行
        while (std::getline(file, line) && i < k) {
            reservoir.push_back(line);
            i++;
        }
        
        // 对后续的行进行采样
        while (std::getline(file, line)) {
            i++;
            std::uniform_int_distribution<int> dist(0, i - 1);
            int j = dist(rng);
            if (j < k) {
                reservoir[j] = line;
            }
        }
        
        file.close();
        return reservoir;
    }
};

/**
 * 扩展题目：数据流中随机采样k个元素
 * 
 * 问题描述：实现一个从无限长的数据流中随机选择k个元素的算法
 * 
 * 解题思路：标准的蓄水池采样算法
 * 时间复杂度：O(n)，其中n是已处理的元素数量
 * 空间复杂度：O(k)
 */
class DataStreamSampler {
private:
    int k; // 采样大小
    std::vector<int> reservoir; // 蓄水池
    int count; // 已处理的元素数量
    std::mt19937 rng; // 随机数生成器

public:
    DataStreamSampler(int k) : k(k), count(0), rng(std::time(nullptr)) {
        reservoir.reserve(k);
    }

    void add(int value) {
        if (count < k) {
            reservoir.push_back(value);
        } else {
            std::uniform_int_distribution<int> dist(0, count);
            int j = dist(rng);
            if (j < k) {
                reservoir[j] = value;
            }
        }
        count++;
    }

    std::vector<int> get_sample() {
        return reservoir;
    }
};

/**
 * 扩展题目：加权随机采样
 * 
 * 问题描述：从一个加权集合中随机选择一个元素，选择的概率与元素的权重成正比
 * 
 * 解题思路：使用前缀和方法
 * 时间复杂度：O(n) 每次查询
 * 空间复杂度：O(n)
 */
class WeightedSampler {
private:
    std::vector<int> nums;
    std::vector<int> weights;
    int total_weight;
    std::mt19937 rng;

public:
    WeightedSampler(const std::vector<int>& nums, const std::vector<int>& weights) : 
        nums(nums), weights(weights), rng(std::time(nullptr)) {
        total_weight = 0;
        for (int w : weights) {
            total_weight += w;
        }
    }

    int pick_index() {
        std::uniform_int_distribution<int> dist(1, total_weight);
        int rand = dist(rng);
        int sum_weight = 0;
        
        for (size_t i = 0; i < weights.size(); i++) {
            sum_weight += weights[i];
            if (rand <= sum_weight) {
                return nums[i];
            }
        }
        
        return nums[0]; // 理论上不会执行到这里
    }
};

/**
 * 单元测试辅助方法：验证采样的等概率性
 */
void validate_uniformity(const std::vector<int>& results, int n, int expected_count) {
    std::unordered_map<int, int> count_map;
    for (int result : results) {
        count_map[result]++;
    }
    
    std::cout << "采样均匀性分析:" << std::endl;
    for (int i = 0; i < n; i++) {
        int actual = count_map.count(i) ? count_map[i] : 0;
        bool within_range = std::abs(actual - expected_count) <= expected_count * 0.05;
        std::cout << "元素 " << i << ": 期望=" << expected_count 
                  << ", 实际=" << actual 
                  << ", " << (within_range ? "通过" : "不通过") << std::endl;
    }
}

/**
 * 测试基础的蓄水池采样算法
 */
void testReservoirSampling() {
    std::cout << "=== 基础蓄水池采样测试 ===" << std::endl;
    std::cout << "测试开始" << std::endl;
    int n = 41; // 一共吐出多少球
    int m = 10; // 袋子大小多少
    int test_times = 10000; // 进行多少次实验
    std::vector<int> cnt(n + 1, 0);
    
    for (int t = 0; t < test_times; t++) {
        Pool pool(m);
        for (int i = 1; i <= n; i++) {
            pool.enter(i);
        }
        std::vector<int> bag = pool.get_bag();
        for (int num : bag) {
            cnt[num]++;
        }
    }
    
    std::cout << "机器吐出到" << n << "号球, 袋子大小为" << m << std::endl;
    std::cout << "每个球被选中的概率应该接近" << static_cast<double>(m) / n << std::endl;
    std::cout << "一共测试" << test_times << "次" << std::endl;
    for (int i = 1; i <= n; i++) {
        std::cout << i << "被选中次数 : " << cnt[i] 
                  << ", 被选中概率 : " << static_cast<double>(cnt[i]) / test_times << std::endl;
    }
    std::cout << "测试结束" << std::endl;
}

/**
 * 测试LeetCode 710. 黑名单中的随机数
 */
void testBlacklistRandom() {
    std::cout << "\n=== LeetCode 710. 黑名单中的随机数测试 ===" << std::endl;
    int n = 10;
    std::vector<int> blacklist = {2, 3, 5};
    SolutionBlacklistRandom solution(n, blacklist);
    std::cout << "随机选择10次不在黑名单中的数:" << std::endl;
    for (int i = 0; i < 10; i++) {
        std::cout << "选中数字: " << solution.pick() << std::endl;
    }
}

/**
 * 测试数据流随机采样
 */
void testDataStreamSampling() {
    std::cout << "\n=== 数据流随机采样测试 ===" << std::endl;
    DataStreamSampler sampler(5);
    for (int i = 1; i <= 100; i++) {
        sampler.add(i);
    }
    std::cout << "从100个元素中随机采样5个:" << std::endl;
    std::vector<int> sample = sampler.get_sample();
    for (int num : sample) {
        std::cout << num << " " << std::endl;
    }
}

/**
 * 测试加权随机采样
 */
void testWeightedSampling() {
    std::cout << "\n=== 加权随机采样测试 ===" << std::endl;
    std::vector<int> nums = {1, 2, 3};
    std::vector<int> weights = {1, 2, 3}; // 权重分别为1,2,3
    WeightedSampler sampler(nums, weights);
    
    std::unordered_map<int, int> weighted_count;
    const int test_times = 10000;
    for (int i = 0; i < test_times; i++) {
        int result = sampler.pick_index();
        weighted_count[result]++;
    }
    
    std::cout << "权重为[1,2,3]的元素采样结果统计:" << std::endl;
    for (const auto& pair : weighted_count) {
        std::cout << "元素 " << pair.first << ": 被选中" << pair.second 
                  << "次, 概率" << static_cast<double>(pair.second) / test_times << std::endl;
    }
}

/**
 * 打印算法复杂度分析
 */
void printComplexityAnalysis() {
    std::cout << "\n=== 算法时间复杂度分析 ===" << std::endl;
    std::cout << "基础蓄水池采样: O(n) 时间, O(k) 空间" << std::endl;
    std::cout << "链表随机节点: O(n) 时间, O(1) 空间" << std::endl;
    std::cout << "随机数索引: O(n) 时间, O(1) 空间" << std::endl;
    std::cout << "黑名单随机数: O(B) 初始化, O(1) 查询, O(B) 空间" << std::endl;
    std::cout << "加权随机采样: O(n) 查询, O(n) 空间" << std::endl;
}

/**
 * 打印C++特有优化技巧
 */
void printCppOptimizationTips() {
    std::cout << "\n=== C++特有优化技巧 ===" << std::endl;
    std::cout << "1. 使用std::mt19937替代rand()以获得更好的随机分布特性" << std::endl;
    std::cout << "2. 使用reserve()提前分配内存，避免频繁的内存重新分配" << std::endl;
    std::cout << "3. 对于频繁查找的集合，使用unordered_set和unordered_map以获得O(1)的平均查找时间" << std::endl;
    std::cout << "4. 对于文件操作，确保正确关闭文件以避免资源泄露" << std::endl;
    std::cout << "5. 使用std::chrono进行精确的性能测量" << std::endl;
    std::cout << "6. 注意异常处理，提高代码的健壮性" << std::endl;
    std::cout << "7. 使用uniform_int_distribution和uniform_real_distribution确保均匀分布" << std::endl;
}

int main() {
    testReservoirSampling();
    testLinkedListRandomNode();
    testRandomPickIndex();
    testBlacklistRandom();
    testDataStreamSampling();
    testWeightedSampling();
    printComplexityAnalysis();
    printCppOptimizationTips();
    
    return 0;
}