// 布隆过滤器实现 (C++版本)
// 题目来源: 大数据处理、缓存系统、网络爬虫去重
// 应用场景: 网页去重、垃圾邮件过滤、缓存穿透防护
// 题目描述: 实现布隆过滤器，支持元素添加和存在性检查
// 
// 解题思路:
// 1. 使用多个哈希函数将元素映射到位数组的不同位置
// 2. 添加元素时，将所有哈希位置设为1
// 3. 检查元素时，如果所有哈希位置都为1，则元素可能存在
// 4. 使用误判率公式计算最优的哈希函数数量和位数组大小
// 
// 时间复杂度分析:
// - 添加元素: O(k)，其中k是哈希函数数量
// - 检查元素: O(k)
// 
// 空间复杂度: O(m)，其中m是位数组大小
// 
// 工程化考量:
// 1. 误判率控制: 根据预期元素数量和可接受的误判率计算最优参数
// 2. 哈希函数选择: 使用不同的哈希函数减少冲突
// 3. 内存优化: 使用位数组节省空间
// 4. 线程安全: 在多线程环境下安全使用

#include <iostream>
#include <vector>
#include <bitset>
#include <functional>
#include <random>
#include <cmath>
#include <stdexcept>
#include <string>
#include <chrono>

using namespace std;

class BloomFilter {
private:
    // 位数组，用于存储元素的存在性信息
    vector<bool> bitSet;
    
    // 位数组大小
    size_t bitSetSize;
    
    // 哈希函数数量
    size_t hashFunctionCount;
    
    // 预期元素数量
    size_t expectedElementCount;
    
    // 实际添加的元素数量
    size_t actualElementCount;
    
    // 哈希函数种子
    vector<size_t> seeds;
    
    /**
     * 哈希函数 - 使用简单的字符串哈希算法
     * @param str 输入字符串
     * @param seed 哈希种子
     * @return 哈希值
     */
    size_t hash(const string& str, size_t seed) const {
        size_t hash = seed;
        for (char c : str) {
            hash = hash * 31 + c;
        }
        return hash;
    }

public:
    /**
     * 构造函数 - 根据预期元素数量和误判率自动计算参数
     * @param expectedElementCount 预期元素数量
     * @param falsePositiveRate 可接受的误判率 (0 < falsePositiveRate < 1)
     * @throws invalid_argument 如果参数无效
     */
    BloomFilter(size_t expectedElementCount, double falsePositiveRate) {
        if (expectedElementCount <= 0) {
            throw invalid_argument("预期元素数量必须大于0");
        }
        if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
            throw invalid_argument("误判率必须在0和1之间");
        }
        
        this->expectedElementCount = expectedElementCount;
        this->actualElementCount = 0;
        
        // 根据误判率公式计算最优参数
        // m = - (n * ln(p)) / (ln(2))^2
        // k = (m / n) * ln(2)
        this->bitSetSize = static_cast<size_t>(
            ceil(-expectedElementCount * log(falsePositiveRate) / (log(2) * log(2)))
        );
        this->hashFunctionCount = static_cast<size_t>(
            ceil((bitSetSize / static_cast<double>(expectedElementCount)) * log(2))
        );
        
        // 确保哈希函数数量至少为1
        this->hashFunctionCount = max(static_cast<size_t>(1), this->hashFunctionCount);
        
        // 初始化位数组
        this->bitSet.resize(bitSetSize, false);
        
        // 初始化哈希种子
        this->seeds.resize(hashFunctionCount);
        mt19937 gen(42); // 固定种子保证可重复性
        uniform_int_distribution<size_t> dis(1, 1000000);
        
        for (size_t i = 0; i < hashFunctionCount; i++) {
            seeds[i] = dis(gen);
        }
        
        cout << "布隆过滤器初始化: 预期元素数=" << expectedElementCount 
             << ", 误判率=" << falsePositiveRate 
             << ", 位数组大小=" << bitSetSize 
             << ", 哈希函数数=" << hashFunctionCount << endl;
    }
    
    /**
     * 构造函数 - 手动指定参数
     * @param bitSetSize 位数组大小
     * @param hashFunctionCount 哈希函数数量
     */
    BloomFilter(size_t bitSetSize, size_t hashFunctionCount) {
        if (bitSetSize <= 0) {
            throw invalid_argument("位数组大小必须大于0");
        }
        if (hashFunctionCount <= 0) {
            throw invalid_argument("哈希函数数量必须大于0");
        }
        
        this->bitSetSize = bitSetSize;
        this->hashFunctionCount = hashFunctionCount;
        this->expectedElementCount = 0; // 未知预期数量
        this->actualElementCount = 0;
        this->bitSet.resize(bitSetSize, false);
        
        // 初始化哈希种子
        this->seeds.resize(hashFunctionCount);
        mt19937 gen(42);
        uniform_int_distribution<size_t> dis(1, 1000000);
        
        for (size_t i = 0; i < hashFunctionCount; i++) {
            seeds[i] = dis(gen);
        }
    }
    
    /**
     * 添加元素到布隆过滤器
     * @param element 要添加的元素
     * @throws invalid_argument 如果元素为空
     */
    void add(const string& element) {
        if (element.empty()) {
            throw invalid_argument("元素不能为空");
        }
        
        // 计算所有哈希位置并设置为1
        for (size_t i = 0; i < hashFunctionCount; i++) {
            size_t hashValue = hash(element, seeds[i]);
            size_t position = hashValue % bitSetSize;
            bitSet[position] = true;
        }
        
        actualElementCount++;
    }
    
    /**
     * 检查元素是否可能在布隆过滤器中
     * @param element 要检查的元素
     * @return true如果元素可能存在，false如果元素一定不存在
     * @throws invalid_argument 如果元素为空
     */
    bool mightContain(const string& element) const {
        if (element.empty()) {
            throw invalid_argument("元素不能为空");
        }
        
        // 检查所有哈希位置是否都为1
        for (size_t i = 0; i < hashFunctionCount; i++) {
            size_t hashValue = hash(element, seeds[i]);
            size_t position = hashValue % bitSetSize;
            if (!bitSet[position]) {
                return false; // 如果有一个位置为0，元素一定不存在
            }
        }
        
        return true; // 所有位置都为1，元素可能存在
    }
    
    /**
     * 获取布隆过滤器的状态信息
     * @return 状态信息字符串
     */
    string getStatus() const {
        size_t setBits = 0;
        for (bool bit : bitSet) {
            if (bit) setBits++;
        }
        
        double fillRatio = static_cast<double>(setBits) / bitSetSize;
        
        // 计算当前误判率
        double currentFalsePositiveRate = pow(fillRatio, hashFunctionCount);
        
        string result;
        result += "布隆过滤器状态:\n";
        result += "位数组大小: " + to_string(bitSetSize) + "\n";
        result += "哈希函数数量: " + to_string(hashFunctionCount) + "\n";
        result += "预期元素数量: " + to_string(expectedElementCount) + "\n";
        result += "实际元素数量: " + to_string(actualElementCount) + "\n";
        result += "已设置位数: " + to_string(setBits) + "\n";
        result += "填充比例: " + to_string(fillRatio) + "\n";
        result += "当前误判率: " + to_string(currentFalsePositiveRate) + "\n";
        
        return result;
    }
    
    /**
     * 性能测试 - 测试布隆过滤器的误判率
     * @param testElementCount 测试元素数量
     */
    void performanceTest(size_t testElementCount) {
        if (testElementCount <= 0) {
            cout << "测试元素数量必须大于0" << endl;
            return;
        }
        
        cout << "=== 布隆过滤器性能测试 ===" << endl;
        cout << "测试元素数量: " << testElementCount << endl;
        
        // 添加测试元素
        size_t falsePositives = 0;
        size_t trueNegatives = 0;
        
        // 添加真实元素
        for (size_t i = 0; i < testElementCount; i++) {
            add("real" + to_string(i));
        }
        
        // 测试不存在元素
        for (size_t i = 0; i < testElementCount; i++) {
            string fakeElement = "fake" + to_string(i);
            if (mightContain(fakeElement)) {
                falsePositives++; // 误判
            } else {
                trueNegatives++; // 正确判断
            }
        }
        
        double actualFalsePositiveRate = static_cast<double>(falsePositives) / testElementCount;
        
        cout << "测试结果:" << endl;
        cout << "误判数量: " << falsePositives << endl;
        cout << "正确判断数量: " << trueNegatives << endl;
        cout << "实际误判率: " << actualFalsePositiveRate << endl;
        
        size_t setBits = 0;
        for (bool bit : bitSet) {
            if (bit) setBits++;
        }
        double theoreticalFalsePositiveRate = pow(static_cast<double>(setBits) / bitSetSize, hashFunctionCount);
        cout << "理论误判率: " << theoreticalFalsePositiveRate << endl;
        
        cout << getStatus() << endl;
    }
    
    /**
     * 获取位数组大小
     * @return 位数组大小
     */
    size_t getBitSetSize() const {
        return bitSetSize;
    }
    
    /**
     * 获取哈希函数数量
     * @return 哈希函数数量
     */
    size_t getHashFunctionCount() const {
        return hashFunctionCount;
    }
    
    /**
     * 获取实际元素数量
     * @return 实际元素数量
     */
    size_t getActualElementCount() const {
        return actualElementCount;
    }
};

/**
 * 单元测试函数
 */
void testBloomFilter() {
    cout << "=== 布隆过滤器单元测试 ===" << endl;
    
    // 测试1: 基本功能测试
    cout << "测试1: 基本功能测试" << endl;
    BloomFilter bf(1000, 0.01);
    
    // 添加元素
    bf.add("apple");
    bf.add("banana");
    bf.add("cherry");
    
    // 检查存在的元素
    if (bf.mightContain("apple") && bf.mightContain("banana") && bf.mightContain("cherry")) {
        cout << "存在性检查测试通过" << endl;
    } else {
        cout << "存在性检查测试失败" << endl;
    }
    
    // 检查不存在的元素
    if (!bf.mightContain("orange") && !bf.mightContain("grape")) {
        cout << "不存在性检查测试通过" << endl;
    } else {
        cout << "不存在性检查测试失败" << endl;
    }
    
    // 测试2: 性能测试
    cout << "\n测试2: 性能测试" << endl;
    bf.performanceTest(1000);
    
    // 测试3: 不同参数对比
    cout << "\n测试3: 不同参数对比" << endl;
    
    BloomFilter bf1(1000, 0.1);  // 高误判率
    BloomFilter bf2(1000, 0.01); // 低误判率
    BloomFilter bf3(1000, 0.001); // 极低误判率
    
    // 添加相同元素
    for (int i = 0; i < 500; i++) {
        bf1.add("test" + to_string(i));
        bf2.add("test" + to_string(i));
        bf3.add("test" + to_string(i));
    }
    
    // 测试误判率
    size_t fp1 = 0, fp2 = 0, fp3 = 0;
    for (int i = 500; i < 1000; i++) {
        if (bf1.mightContain("test" + to_string(i))) fp1++;
        if (bf2.mightContain("test" + to_string(i))) fp2++;
        if (bf3.mightContain("test" + to_string(i))) fp3++;
    }
    
    cout << "不同误判率配置的测试结果:" << endl;
    cout << "误判率0.1: 实际误判率=" << static_cast<double>(fp1)/500 << endl;
    cout << "误判率0.01: 实际误判率=" << static_cast<double>(fp2)/500 << endl;
    cout << "误判率0.001: 实际误判率=" << static_cast<double>(fp3)/500 << endl;
    
    // 测试4: 异常处理测试
    cout << "\n测试4: 异常处理测试" << endl;
    try {
        bf.add(""); // 空元素
        cout << "异常处理测试失败" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常处理测试通过: " << e.what() << endl;
    }
    
    cout << "=== 单元测试完成 ===" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 布隆过滤器性能测试 ===" << endl;
    
    // 测试不同规模的布隆过滤器
    vector<pair<size_t, double>> testCases = {
        {1000, 0.01},
        {10000, 0.01},
        {100000, 0.01}
    };
    
    for (const auto& testCase : testCases) {
        size_t expectedCount = testCase.first;
        double falsePositiveRate = testCase.second;
        
        cout << "\n测试规模: 预期元素数=" << expectedCount 
             << ", 误判率=" << falsePositiveRate << endl;
        
        BloomFilter bf(expectedCount, falsePositiveRate);
        
        // 测试添加性能
        auto start = chrono::high_resolution_clock::now();
        
        for (size_t i = 0; i < expectedCount; i++) {
            bf.add("element" + to_string(i));
        }
        
        auto end = chrono::high_resolution_clock::now();
        auto addDuration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        // 测试查询性能
        start = chrono::high_resolution_clock::now();
        
        size_t found = 0;
        for (size_t i = 0; i < expectedCount; i++) {
            if (bf.mightContain("element" + to_string(i))) {
                found++;
            }
        }
        
        end = chrono::high_resolution_clock::now();
        auto queryDuration = chrono::duration_cast<chrono::milliseconds>(end - start);
        
        cout << "性能测试结果:" << endl;
        cout << "添加时间: " << addDuration.count() << " 毫秒" << endl;
        cout << "平均添加时间: " << (double)addDuration.count() / expectedCount << " 毫秒/元素" << endl;
        cout << "查询时间: " << queryDuration.count() << " 毫秒" << endl;
        cout << "平均查询时间: " << (double)queryDuration.count() / expectedCount << " 毫秒/元素" << endl;
        cout << "正确查询数量: " << found << "/" << expectedCount << endl;
    }
    
    cout << "=== 性能测试完成 ===" << endl;
}

int main() {
    // 运行单元测试
    testBloomFilter();
    
    // 运行性能测试
    performanceTest();
    
    // 演示示例
    cout << "=== 布隆过滤器演示 ===" << endl;
    
    // 创建一个预期处理10000个元素，误判率为1%的布隆过滤器
    BloomFilter bloomFilter(10000, 0.01);
    
    // 添加一些URL到布隆过滤器（模拟网页去重）
    vector<string> urls = {
        "https://example.com/page1",
        "https://example.com/page2", 
        "https://example.com/page3",
        "https://example.com/page4",
        "https://example.com/page5"
    };
    
    for (const auto& url : urls) {
        bloomFilter.add(url);
        cout << "添加URL: " << url << endl;
    }
    
    // 检查URL是否已存在
    vector<string> testUrls = {
        "https://example.com/page1",     // 已存在
        "https://example.com/page6",     // 不存在
        "https://example.com/page3",     // 已存在
        "https://example.com/page7"      // 不存在
    };
    
    cout << "\nURL存在性检查:" << endl;
    for (const auto& url : testUrls) {
        bool exists = bloomFilter.mightContain(url);
        cout << "URL " << url << ": " << (exists ? "可能已存在" : "一定不存在") << endl;
    }
    
    // 显示布隆过滤器状态
    cout << "\n" << bloomFilter.getStatus() << endl;
    
    // 性能测试
    bloomFilter.performanceTest(1000);
    
    return 0;
}