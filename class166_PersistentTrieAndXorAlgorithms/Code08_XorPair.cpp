// 最大异或对
// 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
// 1 <= nums.length <= 2 * 10^5
// 0 <= nums[i] <= 2^31 - 1
// 测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// 测试链接 : https://www.luogu.com.cn/problem/P4551

// 补充题目1: 最大异或子数组
// 给定一个非负整数数组 nums，返回该数组中异或和最大的非空子数组的异或和
// 测试链接: https://leetcode.cn/problems/maximum-xor-subarray/
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-subarray/
// - https://www.hdu.edu.cn/problem/5325
// - https://codeforces.com/problemset/problem/1715/E

// 补充题目2: 子集异或和最大值
// 给定一个非负整数数组 nums，返回所有可能的子集异或和中的最大值
// 测试链接: https://leetcode.cn/problems/maximum-xor-sum-of-a-subarray/
// 相关题目:
// - https://www.luogu.com.cn/problem/P3812
// - https://www.hdu.edu.cn/problem/3949
// - https://codeforces.com/problemset/problem/959/F

// 补充题目3: 寻找异或值为零的三元组
// 给定一个整数数组 arr，返回异或值为0的三元组(i,j,k)的数量，其中 i<j<k
// 测试链接: https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
// 相关题目:
// - https://leetcode.cn/problems/count-triplets-that-can-form-two-arrays-of-equal-xor/
// - https://www.luogu.com.cn/problem/P4592
// - https://codeforces.com/problemset/problem/1175/G

// 由于环境中C++标准库识别有问题，这里提供算法的核心实现思路和注释

/*
 * Trie节点类 - 使用数组实现子节点（0和1两个子节点）
 */
class TrieNode {
public:
    // children存储子节点，索引0表示bit=0，索引1表示bit=1
    TrieNode* children[2];

    // 构造函数
    TrieNode() {
        children[0] = nullptr;
        children[1] = nullptr;
    }

    // 析构函数，释放所有子节点内存
    ~TrieNode() {
        if (children[0]) delete children[0];
        if (children[1]) delete children[1];
    }
};

/*
 * 最大异或对解决方案
 */
class XorPair {
private:
    TrieNode* root;  // Trie树根节点

public:
    // 构造函数，初始化根节点
    XorPair() {
        root = new TrieNode();
    }

    // 析构函数，释放根节点内存
    ~XorPair() {
        delete root;
    }

    /**
     * 向Trie中插入一个数字的二进制表示
     * @param num 要插入的数字
     */
    void insert(int num) {
        TrieNode* node = root;
        // 从最高位（31位）开始插入，逐位处理
        for (int i = 31; i >= 0; i--) {
            // 提取第i位的值（0或1）
            int bit = (num >> i) & 1;
            // 如果该位对应的子节点不存在，则创建新节点
            if (node->children[bit] == nullptr) {
                node->children[bit] = new TrieNode();
            }
            // 移动到子节点
            node = node->children[bit];
        }
    }

    /**
     * 查询与给定数字异或能得到的最大值
     * @param num 给定数字
     * @return 最大异或值
     */
    int getMaxXor(int num) {
        TrieNode* node = root;
        int maxXor = 0;  // 存储最大异或值

        // 从最高位开始处理，贪心策略选择使异或结果最大的路径
        for (int i = 31; i >= 0; i--) {
            // 提取num的第i位
            int bit = (num >> i) & 1;
            // 贪心策略：尽量选择与当前位相反的路径以使异或结果最大
            int desiredBit = bit ^ 1;

            // 如果相反位存在，则选择该路径
            if (node->children[desiredBit] != nullptr) {
                // 将第i位置为1（异或结果为1）
                maxXor |= (1 << i);
                node = node->children[desiredBit];
            } else {
                // 否则只能选择相同位
                if (node->children[bit] != nullptr) {
                    node = node->children[bit];
                } else {
                    // 如果都为空，说明Trie为空，直接返回0
                    break;
                }
            }
        }

        return maxXor;
    }

    /**
     * 找出数组中任意两个数的最大异或值
     * @param nums 输入数组
     * @param n 数组长度
     * @return 最大异或值
     */
    int findMaximumXOR(int nums[], int n) {
        // 边界检查
        if (n <= 0) {
            return 0;
        }

        int maxXor = 0;
        
        // 插入所有元素到Trie中
        for (int i = 0; i < n; i++) {
            insert(nums[i]);
        }

        // 对每个元素，查找与其异或能得到的最大值
        for (int i = 0; i < n; i++) {
            maxXor = (maxXor > getMaxXor(nums[i])) ? maxXor : getMaxXor(nums[i]);
        }

        return maxXor;
    }
};

/*
 * 最大异或子数组解决方案
 */
class MaxXorSubarray {
private:
    TrieNode* root;  // Trie树根节点

public:
    // 构造函数，初始化根节点
    MaxXorSubarray() {
        root = new TrieNode();
    }

    // 析构函数，释放根节点内存
    ~MaxXorSubarray() {
        delete root;
    }

    /**
     * 向Trie中插入一个数字的二进制表示
     * @param num 要插入的数字（前缀异或和）
     */
    void insert(int num) {
        TrieNode* node = root;
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            // 提取第i位的值
            int bit = (num >> i) & 1;
            // 如果该位对应的子节点不存在，则创建新节点
            if (node->children[bit] == nullptr) {
                node->children[bit] = new TrieNode();
            }
            // 移动到子节点
            node = node->children[bit];
        }
    }

    /**
     * 查询与给定数字异或能得到的最大值
     * @param num 当前前缀异或和
     * @return 最大异或值
     */
    int queryMaxXor(int num) {
        TrieNode* node = root;
        int maxXor = 0;  // 存储最大异或值

        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            // 提取num的第i位
            int bit = (num >> i) & 1;
            // 贪心策略：尽量选择与当前位相反的路径
            int desiredBit = bit ^ 1;

            // 如果相反位存在，则选择该路径
            if (node->children[desiredBit] != nullptr) {
                // 将第i位置为1
                maxXor |= (1 << i);
                node = node->children[desiredBit];
            } else {
                // 如果相同位存在，则选择该路径
                if (node->children[bit] != nullptr) {
                    node = node->children[bit];
                } else {
                    // 如果都不存在，跳出循环
                    break;
                }
            }
        }

        return maxXor;
    }

    /**
     * 找出数组中异或和最大的非空子数组的异或和
     * 利用前缀异或和的性质：子数组异或和 = 两个前缀异或和的异或
     * @param nums 输入数组
     * @param n 数组长度
     * @return 最大异或子数组的异或和
     */
    int maxXorSubarray(int nums[], int n) {
        // 边界检查
        if (n <= 0) {
            return 0;
        }

        // 使用较小的初始值代替INT_MIN
        int maxXor = -2147483648;  // 32位整数的最小值
        int prefixXor = 0;     // 当前前缀异或和

        // 插入前缀异或和0，表示空数组的情况
        insert(0);

        // 遍历数组元素
        for (int i = 0; i < n; i++) {
            // 计算当前前缀异或和
            prefixXor ^= nums[i];

            // 查询当前前缀异或和与之前前缀异或和的最大异或值
            int currentMax = queryMaxXor(prefixXor);
            maxXor = (maxXor > currentMax) ? maxXor : currentMax;

            // 插入当前前缀异或和
            insert(prefixXor);
        }

        return maxXor;
    }
};

/*
 * 子集异或和最大值解决方案 - 使用线性基
 */
class MaxXorSubset {
public:
    /**
     * 计算所有可能的子集异或和中的最大值
     * 方法：高斯消元，构建线性基
     * @param nums 输入数组
     * @param n 数组长度
     * @return 最大子集异或和
     */
    int maxXorSubset(int nums[], int n) {
        // 边界检查
        if (n <= 0) {
            return 0;
        }

        // 线性基数组，base[i]表示第i位为最高位的数
        int base[32] = {0};

        // 构建线性基
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            // 为0的元素可以直接跳过
            if (num == 0) {
                continue;
            }

            // 从最高位开始处理
            for (int j = 31; j >= 0; j--) {
                // 如果num的第j位为1
                if (((num >> j) & 1) == 1) {
                    // 如果该位没有被占据，则插入到线性基中
                    if (base[j] == 0) {
                        base[j] = num;
                        break;
                    }
                    // 否则，将当前数与线性基中对应的数异或，继续处理
                    // 这类似于高斯消元的过程
                    num ^= base[j];
                }
            }
        }

        // 计算最大异或和
        int result = 0;
        // 从最高位开始贪心选择
        for (int i = 31; i >= 0; i--) {
            // 尝试用当前基向量异或，看是否能使结果更大
            if ((result ^ base[i]) > result) {
                result ^= base[i];
            }
        }

        return result;
    }
};

/*
 * 寻找异或值为零的三元组解决方案
 */
class TripletXorZero {
public:
    /**
     * 暴力解法：计算异或值为0的三元组(i,j,k)的数量
     * 时间复杂度: O(n^3) - 对于每个i，遍历所有可能的j和k
     * @param arr 输入数组
     * @param n 数组长度
     * @return 异或值为0的三元组数量
     */
    int countTripletsBruteForce(int arr[], int n) {
        // 边界检查
        if (n < 3) {
            return 0;
        }

        int result = 0;

        // 遍历所有可能的i, j, k组合
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                // 计算a[i] ^ a[j]
                int xorSum = arr[i] ^ arr[j];
                for (int k = j + 1; k < n; k++) {
                    // 计算a[i] ^ a[j] ^ a[k]
                    xorSum ^= arr[k];
                    // 如果异或和为0，则找到一个满足条件的三元组
                    if (xorSum == 0) {
                        result++;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 优化解法1：使用前缀异或和和两重循环
     * 时间复杂度: O(n^2)
     * 数学原理：如果a[i]^a[i+1]^...^a[k] = 0，那么对于任意i<j<=k，
     * 都有a[i+1]^...^a[j] = a[j+1]^...^a[k]
     * @param arr 输入数组
     * @param n 数组长度
     * @return 异或值为0的三元组数量
     */
    int countTripletsOptimized1(int arr[], int n) {
        // 边界检查
        if (n < 3) {
            return 0;
        }

        int result = 0;

        // 遍历所有可能的i和k
        for (int i = 0; i < n; i++) {
            int xorSum = 0;
            for (int k = i; k < n; k++) {
                // 计算a[i]^a[i+1]^...^a[k]
                xorSum ^= arr[k];
                // 如果从i到k的异或和为0，那么中间的任意j(i<j<=k)都满足条件
                // 这样的j有(k-i)个
                if (xorSum == 0 && k > i) {
                    result += (k - i);
                }
            }
        }

        return result;
    }
};

// 主函数示例（由于环境限制，这里只是示意）
int main() {
    // 由于环境中C++标准库识别有问题，这里不提供完整的main函数实现
    // 以上类和方法提供了完整的算法实现
    
    return 0;
}

/*
算法分析总结：

1. 最大异或对 (XorPair)
时间复杂度: O(n * log M)
- n是数组长度
- log M是数字的位数（这里M=2^31，所以log M=32）
空间复杂度: O(n * log M)
- 最坏情况下，Trie需要存储所有数字的所有位
核心思想: 使用Trie树和贪心策略，从最高位开始，尽量选择与当前位相反的路径
优化点: 使用数组实现Trie节点，提高访问效率；手动管理内存避免泄漏

2. 最大异或子数组 (MaxXorSubarray)
时间复杂度: O(n * log M)
空间复杂度: O(n)
核心思想: 利用前缀异或和的性质（子数组异或和 = 两个前缀异或和的异或），结合Trie树查找最大异或值
关键点: 插入前缀异或和0，处理i=0的特殊情况
数学原理: 对于数组a[0...n-1]，前缀异或和为prefixXor[i] = a[0]^a[1]^...^a[i-1]
          则子数组a[i...j]的异或和 = prefixXor[j+1] ^ prefixXor[i]

3. 子集异或和最大值 (MaxXorSubset)
时间复杂度: O(n * log M)
空间复杂度: O(log M)
核心思想: 线性基（高斯消元思想），将每个数分解到不同的最高位，贪心选择最大异或组合
优点: 线性基可以表示所有可能的异或结果，且能高效求出最大值
数学原理: 任何数都可以表示为线性基数组中若干数的异或结果
         每个数被插入到其最高位对应的位置，类似高斯消元过程
优化点: 对于为0的元素直接跳过，避免不必要的计算

4. 寻找异或值为零的三元组 (TripletXorZero)
暴力解法: O(n^3)，只适用于小数据量
优化解法1: O(n^2)，枚举i和k，计算异或和
数学原理: 如果prefixXor[i] = prefixXor[k+1]，则子数组[i+1,k]的异或和为0
          此时对于任意i < j <= k，都有a[i+1]^...^a[j] = a[j+1]^...^a[k]

工程化考量：
1. 内存管理：使用析构函数正确释放Trie树内存，避免内存泄漏
2. 异常防御：在查询时进行了空指针检查，处理各种边界情况
3. 边界处理：所有方法都处理了空数组和小规模数组的情况
4. 性能优化：
   - 使用数组代替map，提高访问效率
   - 使用位运算代替乘除法，提高计算效率
5. 代码模块化：每个问题都封装在单独的类中，便于复用和维护
6. 资源管理：使用RAII原则确保资源正确释放

算法在工程中的应用：
1. 网络安全：异或运算在加密和解密算法中有广泛应用
2. 数据压缩：Trie树结构可用于高效的字符串压缩算法
3. 特征选择：最大异或问题的思想可用于机器学习中的特征提取和降维
4. 计算机视觉：异或运算在图像处理和模式匹配中有特定应用
5. 网络协议：位运算常用于网络数据包的解析和处理
6. 哈希算法：异或操作常用于构造哈希函数

调试技巧：
1. 打印中间变量：在关键步骤打印位运算结果和Trie节点状态
2. 小例子测试：用简单的测试用例验证算法逻辑的正确性
3. 边界测试：测试空数组、单元素数组、全零数组等特殊情况
4. 性能分析：识别性能瓶颈并进行优化

与机器学习的联系：
1. 特征提取：线性基的概念与机器学习中的特征选择和降维有关
2. 决策树：Trie树的结构与决策树算法有相似之处
3. 位操作在深度学习中的应用：神经网络中某些优化算法使用位运算加速计算
4. 哈希学习：哈希表的使用与哈希学习算法中的特征映射有关

算法优化建议：
1. 对于最大异或对问题，可以进一步优化Trie节点的实现
2. 对于大规模数据，可以考虑并行化处理
3. 可以使用位操作的优化技巧，如位移运算代替乘法除法
*/