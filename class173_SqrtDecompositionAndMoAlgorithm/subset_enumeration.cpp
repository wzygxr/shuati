#include <iostream>
#include <vector>
#include <string>

/**
 * 子集枚举算法实现
 * 核心技巧：使用位掩码高效枚举集合的所有子集
 * 时间复杂度：O(2^n)，其中n是元素个数
 */

/**
 * 枚举mask的所有非空子集
 * 核心公式：for (int sub = mask; sub > 0; sub = (sub - 1) & mask)
 * 
 * @param mask 表示原集合的位掩码
 * @return 所有非空子集的位掩码列表
 */
std::vector<int> enumerateAllSubsets(int mask) {
    std::vector<int> subsets;
    // 枚举所有非空子集的经典算法
    for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
        subsets.push_back(sub);
    }
    return subsets;
}

/**
 * 枚举大小为k的子集
 * 使用Gosper's Hack算法
 * 
 * @param n 集合大小
 * @param k 子集大小
 * @return 所有大小为k的子集的位掩码列表
 */
std::vector<int> enumerateSubsetsOfSize(int n, int k) {
    std::vector<int> subsets;
    if (k == 0 || k > n) {
        return subsets;
    }
    
    int mask = (1 << k) - 1; // 初始化为k个1
    
    while (mask < (1 << n)) {
        subsets.push_back(mask);
        
        // Gosper's Hack - 高效生成下一个k元素子集
        int c = mask & -mask;
        int r = mask + c;
        mask = (((r ^ mask) >> 2) / c) | r;
    }
    
    return subsets;
}

/**
 * 枚举包含特定元素的子集
 * 
 * @param mask 原集合的位掩码
 * @param requiredElements 必须包含的元素的位掩码
 * @return 包含所有requiredElements的子集的位掩码列表
 */
std::vector<int> enumerateSubsetsWithRequired(int mask, int requiredElements) {
    std::vector<int> subsets;
    
    // 检查requiredElements是否是mask的子集
    if ((requiredElements & mask) != requiredElements) {
        return subsets; // requiredElements包含mask中没有的元素
    }
    
    // 枚举所有包含requiredElements的子集
    int remaining = mask & (~requiredElements);
    int sub = remaining;
    while (true) {
        subsets.push_back(sub | requiredElements);
        if (sub == 0) break;
        sub = (sub - 1) & remaining;
    }
    
    return subsets;
}

/**
 * 将位掩码转换为元素索引列表
 * 
 * @param mask 位掩码
 * @return 元素索引列表
 */
std::vector<int> maskToIndices(int mask) {
    std::vector<int> indices;
    for (int i = 0; i < 32; i++) {
        if (mask & (1 << i)) {
            indices.push_back(i);
        }
    }
    return indices;
}

/**
 * 计算子集的数量
 * 
 * @param mask 位掩码
 * @return 子集数量（包括空集）
 */
int countSubsets(int mask) {
    // 使用内置函数计算1的个数
    #ifdef __GNUC__
        int cnt = __builtin_popcount(mask);
    #else
        int cnt = 0;
        for (int x = mask; x; x >>= 1) {
            cnt += x & 1;
        }
    #endif
    return 1 << cnt;
}

/**
 * 将整数转换为二进制字符串
 * 
 * @param num 整数
 * @return 二进制字符串
 */
std::string toBinaryString(int num) {
    if (num == 0) return "0";
    std::string result;
    while (num > 0) {
        result = (num % 2 ? '1' : '0') + result;
        num /= 2;
    }
    return result;
}

/**
 * 打印索引列表
 * 
 * @param indices 索引列表
 */
void printIndices(const std::vector<int>& indices) {
    std::cout << "[";
    for (size_t i = 0; i < indices.size(); i++) {
        std::cout << indices[i];
        if (i < indices.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

int main() {
    // 测试1：枚举所有子集
    int mask = 0b1011; // 表示集合{0,1,3}
    std::cout << "原集合(二进制): " << toBinaryString(mask) << std::endl;
    std::cout << "所有非空子集:" << std::endl;
    std::vector<int> subsets = enumerateAllSubsets(mask);
    for (int sub : subsets) {
        std::cout << "  " << toBinaryString(sub) << " -> ";
        printIndices(maskToIndices(sub));
        std::cout << std::endl;
    }
    
    // 测试2：枚举大小为2的子集
    std::cout << "\n大小为2的子集:" << std::endl;
    std::vector<int> size2Subsets = enumerateSubsetsOfSize(4, 2);
    for (int sub : size2Subsets) {
        std::cout << "  " << toBinaryString(sub) << " -> ";
        printIndices(maskToIndices(sub));
        std::cout << std::endl;
    }
    
    // 测试3：枚举包含特定元素的子集
    int required = 0b101; // 必须包含元素0和2
    std::cout << "\n包含元素0和2的子集:" << std::endl;
    std::vector<int> requiredSubsets = enumerateSubsetsWithRequired(0b1111, required);
    for (int sub : requiredSubsets) {
        std::cout << "  " << toBinaryString(sub) << " -> ";
        printIndices(maskToIndices(sub));
        std::cout << std::endl;
    }
    
    return 0;
}