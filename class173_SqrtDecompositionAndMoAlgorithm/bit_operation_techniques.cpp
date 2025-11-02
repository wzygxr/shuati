#include <iostream>
#include <vector>
#include <map>
#include <set>
#include <tuple>
#include <string>
#include <algorithm>
#include <chrono>
#include <random>
#include <bitset>
#include <iomanip>

/**
 * 位运算技巧工具类
 * 提供高效的位操作实现，包括：
 * 1. 子集枚举（使用经典的 for(s=sub; s; s=(s-1)&mask) 模式）
 * 2. Popcount（汉明重量）优化算法
 * 3. 掩码预处理技术
 * 
 * 位运算在算法优化中具有极高的效率，可以将O(n)的操作优化到O(1)或更低
 */
class BitOperationTechniques {
private:
    // 预处理8位数字的popcount查找表
    static const int POPCOUNT_TABLE[256];

public:
    /**
     * 使用经典的子集枚举算法枚举指定掩码的所有非空子集
     * 时间复杂度：O(2^k)，其中k是掩码中1的个数
     * 空间复杂度：O(1)，不计算结果存储
     * 
     * @param mask 要枚举子集的掩码
     * @return 所有非空子集的列表
     */
    static std::vector<int> enumerateSubsets(int mask) {
        std::vector<int> subsets;
        int sub = mask;
        // 经典的子集枚举模式：for(s=sub; s; s=(s-1)&mask)
        do {
            subsets.push_back(sub);
            sub = (sub - 1) & mask;
        } while (sub != mask); // 当回到mask时，表示所有子集都已枚举完成
        return subsets;
    }

    /**
     * 使用位运算枚举所有可能的子集对，满足A是B的子集
     * 时间复杂度：O(3^n)，其中n是位数（对于n位数字，共有3^n种这样的子集对）
     * 
     * @param mask 最大的掩码
     * @return 所有满足A是B的子集对 (A, B) 的列表
     */
    static std::vector<std::pair<int, int>> enumerateSubsetPairs(int mask) {
        std::vector<std::pair<int, int>> pairs;
        // 枚举所有可能的B
        int b = 0;
        do {
            // 枚举B的所有子集A
            int a = b;
            do {
                pairs.push_back(std::make_pair(a, b));
                a = (a - 1) & b;
            } while (a != b);
            
            // 生成下一个B
            b = (b - mask) & mask;
        } while (b != 0 || mask == 0);
        
        return pairs;
    }

    /**
     * 使用查表法计算整数的二进制中1的个数（popcount）
     * 这是一种空间换时间的优化方法，预处理所有可能的字节值
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)，需要256字节的查找表
     * 
     * @param n 要计算的整数
     * @return n的二进制表示中1的个数
     */
    static int popcountTable(int n) {
        // 将整数分解为多个字节，查表累加
        int count = 0;
        count += POPCOUNT_TABLE[n & 0xFF];
        count += POPCOUNT_TABLE[(n >> 8) & 0xFF];
        count += POPCOUNT_TABLE[(n >> 16) & 0xFF];
        count += POPCOUNT_TABLE[(n >> 24) & 0xFF];
        return count;
    }

    /**
     * 使用Brian Kernighan算法计算popcount
     * 时间复杂度：O(k)，其中k是1的个数
     * 空间复杂度：O(1)
     * 
     * @param n 要计算的整数
     * @return n的二进制表示中1的个数
     */
    static int popcountBrianKernighan(int n) {
        int count = 0;
        // 每次循环清除最低位的1，直到n变为0
        while (n != 0) {
            n &= n - 1; // 清除最低位的1
            count++;
        }
        return count;
    }

    /**
     * 使用C++内置函数计算popcount
     * 在支持的编译器上，这会被优化为CPU指令
     * 
     * @param n 要计算的整数
     * @return n的二进制表示中1的个数
     */
    static int popcountBuiltin(int n) {
        return __builtin_popcount(n);
    }

    /**
     * 预处理所有可能的子集掩码，按1的个数分组
     * 这在需要按子集大小处理问题时非常有用
     * 
     * @param n 位数
     * @return 按1的个数分组的子集列表，其中第k个列表包含所有恰好有k个1的掩码
     */
    static std::vector<std::vector<int>> precomputeSubsetsBySize(int n) {
        std::vector<std::vector<int>> subsetsBySize(n + 1);
        
        // 枚举所有可能的子集（0到2^n-1）
        int maxMask = (1 << n) - 1;
        for (int mask = 0; mask <= maxMask; mask++) {
            int count = popcountBuiltin(mask);
            subsetsBySize[count].push_back(mask);
        }
        
        return subsetsBySize;
    }

    /**
     * 预处理所有可能的掩码及其对应的补码
     * 
     * @param n 位数
     * @return 掩码到其补码的映射
     */
    static std::map<int, int> precomputeComplements(int n) {
        std::map<int, int> complements;
        int maxMask = (1 << n) - 1;
        for (int mask = 0; mask <= maxMask; mask++) {
            complements[mask] = mask ^ maxMask; // 异或操作计算补码
        }
        return complements;
    }

    /**
     * 预处理所有可能的掩码及其超集
     * 
     * @param n 位数
     * @return 每个掩码对应的所有超集
     */
    static std::map<int, std::vector<int>> precomputeSupersets(int n) {
        std::map<int, std::vector<int>> supersets;
        int maxMask = (1 << n) - 1;
        
        // 初始化每个掩码的超集列表
        for (int mask = 0; mask <= maxMask; mask++) {
            supersets[mask] = std::vector<int>();
        }
        
        // 对于每个可能的超集
        for (int superset = 0; superset <= maxMask; superset++) {
            // 找出它的所有子集并更新对应子集的超集列表
            int subset = superset;
            do {
                supersets[subset].push_back(superset);
                subset = (subset - 1) & superset;
            } while (subset != superset);
        }
        
        return supersets;
    }

    /**
     * 检查两个掩码是否不相交（没有共同的1位）
     * 时间复杂度：O(1)
     * 
     * @param a 第一个掩码
     * @param b 第二个掩码
     * @return 如果两个掩码不相交返回true，否则返回false
     */
    static bool areDisjoint(int a, int b) {
        return (a & b) == 0;
    }

    /**
     * 计算两个掩码的对称差（异或）
     * 时间复杂度：O(1)
     * 
     * @param a 第一个掩码
     * @param b 第二个掩码
     * @return 对称差的结果
     */
    static int symmetricDifference(int a, int b) {
        return a ^ b;
    }

    /**
     * 获取掩码中最低位的1
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 只保留最低位1的掩码
     */
    static int getLowestSetBit(int mask) {
        // 使用补码性质：-mask 是 mask 的补码
        return mask & -mask;
    }

    /**
     * 获取掩码中最高位的1
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 只保留最高位1的掩码
     */
    static int getHighestSetBit(int mask) {
        if (mask == 0) return 0;
        // 找到最高位1的位置
        int highestBitPosition = 31 - __builtin_clz(mask); // __builtin_clz 计算前导零的个数
        return 1 << highestBitPosition;
    }

    /**
     * 计算掩码中1的最低位置（从0开始计数）
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 最低位1的位置，如果没有1则返回-1
     */
    static int getLowestSetBitPosition(int mask) {
        if (mask == 0) return -1;
        // __builtin_ctz 计算尾随零的个数
        return __builtin_ctz(mask);
    }

    /**
     * 计算掩码中1的最高位置（从0开始计数）
     * 时间复杂度：O(1)
     * 
     * @param mask 输入掩码
     * @return 最高位1的位置，如果没有1则返回-1
     */
    static int getHighestSetBitPosition(int mask) {
        if (mask == 0) return -1;
        // __builtin_clz 计算前导零的个数
        return 31 - __builtin_clz(mask);
    }

    /**
     * 计算所有可能的子集异或和
     * 时间复杂度：O(2^n)
     * 
     * @param nums 输入数组
     * @return 所有子集异或和的列表
     */
    static std::vector<int> calculateSubsetXOR(const std::vector<int>& nums) {
        std::set<int> xors;
        xors.insert(0); // 空集的异或和为0
        
        for (int num : nums) {
            std::set<int> newXors(xors.begin(), xors.end());
            for (int xorValue : xors) {
                newXors.insert(xorValue ^ num);
            }
            xors.swap(newXors);
        }
        
        return std::vector<int>(xors.begin(), xors.end());
    }

    /**
     * 使用位掩码优化的动态规划解决背包问题
     * 时间复杂度：O(2^n)
     * 空间复杂度：O(1)，使用整型掩码表示状态
     * 
     * @param weights 物品重量数组
     * @param values 物品价值数组
     * @param capacity 背包容量
     * @return 最大价值
     */
    static int knapsackWithBitmask(const std::vector<int>& weights, const std::vector<int>& values, int capacity) {
        int n = weights.size();
        int maxValue = 0;
        
        // 枚举所有可能的子集（2^n种可能）
        for (int mask = 0; mask < (1 << n); mask++) {
            int totalWeight = 0;
            int totalValue = 0;
            bool overCapacity = false;
            
            // 计算子集的总重量和总价值
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    totalWeight += weights[i];
                    totalValue += values[i];
                    
                    // 剪枝：如果总重量已经超过容量，提前退出
                    if (totalWeight > capacity) {
                        overCapacity = true;
                        break;
                    }
                }
            }
            
            // 如果总重量不超过容量，更新最大价值
            if (!overCapacity && totalValue > maxValue) {
                maxValue = totalValue;
            }
        }
        
        return maxValue;
    }
};

// 初始化popcount查找表
const int BitOperationTechniques::POPCOUNT_TABLE[256] = {
    0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7,
    4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8
};

/**
 * 将整数转换为固定长度的二进制字符串
 */
std::string toBinaryString(int n, int width) {
    std::string binary = std::bitset<32>(n).to_string();
    // 移除前导零，直到到达指定宽度
    size_t startPos = binary.find('1');
    if (startPos == std::string::npos) {
        return std::string(width, '0'); // 全零
    }
    
    std::string result = binary.substr(startPos);
    // 如果结果长度小于width，在前面补零
    if (result.length() < width) {
        result = std::string(width - result.length(), '0') + result;
    }
    return result;
}

/**
 * 测试位运算技巧的各种功能
 */
void testBitOperations() {
    std::cout << "=== 位运算技巧测试 ===" << std::endl;
    
    // 测试子集枚举
    int mask = 0b1011; // 二进制 1011，十进制 11
    std::cout << "枚举掩码 0b" << std::bitset<4>(mask) << " 的所有子集：" << std::endl;
    std::vector<int> subsets = BitOperationTechniques::enumerateSubsets(mask);
    for (int subset : subsets) {
        std::cout << "0b" << std::bitset<4>(subset) << " (" << subset << ")" << std::endl;
    }
    
    // 测试Popcount方法比较
    std::cout << "\nPopcount方法比较：" << std::endl;
    int testNumber = 0xAAAAAAAA; // 二进制中1的个数为16
    std::cout << "数字 0x" << std::hex << testNumber << std::dec << " 的二进制中1的个数：" << std::endl;
    std::cout << "查表法: " << BitOperationTechniques::popcountTable(testNumber) << std::endl;
    std::cout << "Brian Kernighan算法: " << BitOperationTechniques::popcountBrianKernighan(testNumber) << std::endl;
    std::cout << "C++内置方法: " << BitOperationTechniques::popcountBuiltin(testNumber) << std::endl;
    
    // 测试预处理子集
    std::cout << "\n按1的个数分组的子集（n=4）：" << std::endl;
    std::vector<std::vector<int>> subsetsBySize = BitOperationTechniques::precomputeSubsetsBySize(4);
    for (size_t i = 0; i < subsetsBySize.size(); i++) {
        std::cout << "包含 " << i << " 个1的子集：" << std::endl;
        for (int subset : subsetsBySize[i]) {
            std::cout << "0b" << toBinaryString(subset, 4) << " (" << subset << ")  ";
        }
        std::cout << std::endl;
    }
    
    // 测试位操作辅助函数
    std::cout << "\n位操作辅助函数：" << std::endl;
    int testMask = 0b101010; // 二进制 101010，十进制 42
    std::cout << "掩码 0b" << std::bitset<8>(testMask) << " (" << testMask << ") 的最低位1: 0b" 
              << std::bitset<8>(BitOperationTechniques::getLowestSetBit(testMask)) << std::endl;
    std::cout << "掩码 0b" << std::bitset<8>(testMask) << " (" << testMask << ") 的最高位1: 0b" 
              << std::bitset<8>(BitOperationTechniques::getHighestSetBit(testMask)) << std::endl;
    std::cout << "掩码 0b" << std::bitset<8>(testMask) << " (" << testMask << ") 的最低位1位置: " 
              << BitOperationTechniques::getLowestSetBitPosition(testMask) << std::endl;
    std::cout << "掩码 0b" << std::bitset<8>(testMask) << " (" << testMask << ") 的最高位1位置: " 
              << BitOperationTechniques::getHighestSetBitPosition(testMask) << std::endl;
    
    // 测试子集异或和
    std::cout << "\n子集异或和计算：" << std::endl;
    std::vector<int> nums = {1, 2, 3};
    std::vector<int> xors = BitOperationTechniques::calculateSubsetXOR(nums);
    std::cout << "数组 {1, 2, 3} 的所有子集异或和: [";
    for (size_t i = 0; i < xors.size(); i++) {
        std::cout << xors[i];
        if (i < xors.size() - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    
    // 测试掩码优化的背包问题
    std::cout << "\n掩码优化的背包问题：" << std::endl;
    std::vector<int> weights = {2, 3, 4, 5};
    std::vector<int> values = {3, 4, 5, 6};
    int capacity = 8;
    int maxValue = BitOperationTechniques::knapsackWithBitmask(weights, values, capacity);
    std::cout << "物品重量: {2, 3, 4, 5}" << std::endl;
    std::cout << "物品价值: {3, 4, 5, 6}" << std::endl;
    std::cout << "背包容量: " << capacity << std::endl;
    std::cout << "最大价值: " << maxValue << std::endl;
}

/**
 * 比较不同Popcount实现的性能
 */
void benchmarkPopcount() {
    std::cout << "\n=== Popcount性能基准测试 ===" << std::endl;
    
    const int testIterations = 10000000;
    
    // 生成随机测试数据
    std::vector<int> testNumbers(testIterations);
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dis(0, INT_MAX);
    
    for (int i = 0; i < testIterations; i++) {
        testNumbers[i] = dis(gen);
    }
    
    // 测试查表法性能
    auto start = std::chrono::high_resolution_clock::now();
    int tableSum = 0;
    for (int num : testNumbers) {
        tableSum += BitOperationTechniques::popcountTable(num);
    }
    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> tableTime = end - start;
    
    // 测试Brian Kernighan算法性能
    start = std::chrono::high_resolution_clock::now();
    int bkSum = 0;
    for (int num : testNumbers) {
        bkSum += BitOperationTechniques::popcountBrianKernighan(num);
    }
    end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> bkTime = end - start;
    
    // 测试C++内置方法性能
    start = std::chrono::high_resolution_clock::now();
    int builtinSum = 0;
    for (int num : testNumbers) {
        builtinSum += BitOperationTechniques::popcountBuiltin(num);
    }
    end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> builtinTime = end - start;
    
    // 验证结果一致性
    bool resultsMatch = (tableSum == bkSum) && (bkSum == builtinSum);
    
    std::cout << "测试 " << testIterations << " 次Popcount操作的结果一致性: " << std::boolalpha << resultsMatch << std::endl;
    std::cout << "查表法耗时: " << std::fixed << std::setprecision(6) << tableTime.count() << " 秒" << std::endl;
    std::cout << "Brian Kernighan算法耗时: " << std::fixed << std::setprecision(6) << bkTime.count() << " 秒" << std::endl;
    std::cout << "C++内置方法耗时: " << std::fixed << std::setprecision(6) << builtinTime.count() << " 秒" << std::endl;
    std::cout << "加速比 (查表法/BK): " << std::fixed << std::setprecision(2) << (tableTime.count() / bkTime.count()) << "x" << std::endl;
    std::cout << "加速比 (查表法/内置): " << std::fixed << std::setprecision(2) << (tableTime.count() / builtinTime.count()) << "x" << std::endl;
    std::cout << "加速比 (BK/内置): " << std::fixed << std::setprecision(2) << (bkTime.count() / builtinTime.count()) << "x" << std::endl;
}

/**
 * 交互式测试函数
 */
void interactiveMode() {
    std::cout << "=== 位运算技巧工具 ===" << std::endl;
    std::cout << "输入操作编号:" << std::endl;
    std::cout << "1. 枚举子集" << std::endl;
    std::cout << "2. 计算Popcount" << std::endl;
    std::cout << "3. 位操作辅助函数" << std::endl;
    std::cout << "4. 子集异或和计算" << std::endl;
    std::cout << "5. 掩码优化背包问题" << std::endl;
    std::cout << "6. Popcount性能基准测试" << std::endl;
    std::cout << "0. 退出" << std::endl;
    
    while (true) {
        std::cout << "\n请输入操作编号: ";
        int choice;
        std::cin >> choice;
        
        try {
            switch (choice) {
                case 0:
                    std::cout << "程序已退出" << std::endl;
                    return;
                case 1: {
                    std::cout << "请输入掩码（十进制）: ";
                    int mask;
                    std::cin >> mask;
                    std::vector<int> subsets = BitOperationTechniques::enumerateSubsets(mask);
                    std::cout << "掩码 " << mask << " (0b" << std::bitset<16>(mask) << ") 的所有子集：" << std::endl;
                    for (int subset : subsets) {
                        std::cout << "0b" << std::bitset<16>(subset) << " (" << subset << ")" << std::endl;
                    }
                    break;
                }
                case 2: {
                    std::cout << "请输入要计算Popcount的数字: ";
                    int num;
                    std::cin >> num;
                    std::cout << "数字 " << num << " (0b" << std::bitset<32>(num) << ") 的二进制中1的个数：" << std::endl;
                    std::cout << "查表法: " << BitOperationTechniques::popcountTable(num) << std::endl;
                    std::cout << "Brian Kernighan算法: " << BitOperationTechniques::popcountBrianKernighan(num) << std::endl;
                    std::cout << "C++内置方法: " << BitOperationTechniques::popcountBuiltin(num) << std::endl;
                    break;
                }
                case 3: {
                    std::cout << "请输入掩码（十进制）: ";
                    int testMask;
                    std::cin >> testMask;
                    std::cout << "掩码 " << testMask << " (0b" << std::bitset<16>(testMask) << ") 的位操作结果：" << std::endl;
                    std::cout << "最低位1: 0b" << std::bitset<16>(BitOperationTechniques::getLowestSetBit(testMask)) 
                              << " (" << BitOperationTechniques::getLowestSetBit(testMask) << ")" << std::endl;
                    std::cout << "最高位1: 0b" << std::bitset<16>(BitOperationTechniques::getHighestSetBit(testMask)) 
                              << " (" << BitOperationTechniques::getHighestSetBit(testMask) << ")" << std::endl;
                    std::cout << "最低位1位置: " << BitOperationTechniques::getLowestSetBitPosition(testMask) << std::endl;
                    std::cout << "最高位1位置: " << BitOperationTechniques::getHighestSetBitPosition(testMask) << std::endl;
                    break;
                }
                case 4: {
                    std::cout << "请输入数组元素个数: ";
                    int n;
                    std::cin >> n;
                    std::cout << "请输入数组元素（空格分隔）：" << std::endl;
                    std::vector<int> array(n);
                    for (int i = 0; i < n; i++) {
                        std::cin >> array[i];
                    }
                    std::vector<int> xors = BitOperationTechniques::calculateSubsetXOR(array);
                    std::cout << "数组的所有子集异或和: [";
                    for (size_t i = 0; i < xors.size(); i++) {
                        std::cout << xors[i];
                        if (i < xors.size() - 1) std::cout << ", ";
                    }
                    std::cout << "]" << std::endl;
                    break;
                }
                case 5: {
                    std::cout << "请输入物品个数: ";
                    int itemCount;
                    std::cin >> itemCount;
                    std::cout << "请输入物品的重量和价值（每行一个物品，格式：重量 价值）：" << std::endl;
                    std::vector<int> weights(itemCount);
                    std::vector<int> values(itemCount);
                    for (int i = 0; i < itemCount; i++) {
                        std::cin >> weights[i] >> values[i];
                    }
                    std::cout << "请输入背包容量: ";
                    int capacity;
                    std::cin >> capacity;
                    int maxValue = BitOperationTechniques::knapsackWithBitmask(weights, values, capacity);
                    std::cout << "最大价值: " << maxValue << std::endl;
                    break;
                }
                case 6:
                    benchmarkPopcount();
                    break;
                default:
                    std::cout << "无效的操作编号，请重新输入" << std::endl;
            }
        } catch (const std::exception& e) {
            std::cout << "操作出错: " << e.what() << std::endl;
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        }
    }
}

int main() {
    // 运行测试
    testBitOperations();
    
    // 启动交互模式
    interactiveMode();
    
    return 0;
}