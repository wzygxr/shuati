// LeetCode 995 Minimum Number of K Consecutive Bit Flips
// C++ 实现

/**
 * LeetCode 995 Minimum Number of K Consecutive Bit Flips
 * 
 * 题目描述：
 * 在仅包含 0 和 1 的数组 A 中，可以进行任意次数的翻转操作。
 * 一次翻转操作选择长度为 K 的连续子数组，然后将该子数组中的每个值都翻转。
 * 返回使数组变为全1所需的最少翻转次数。如果不可能，返回-1。
 * 
 * 解题思路：
 * 我们可以使用差分数组来优化翻转操作的记录。
 * 1. 遍历数组，当遇到0时，需要进行翻转操作
 * 2. 使用差分数组记录翻转操作的影响范围
 * 3. 通过前缀和计算当前位置的实际翻转次数
 * 4. 根据实际翻转次数判断当前位置的值
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
int minKBitFlips(int* A, int ASize, int K) {
    // 创建差分数组记录翻转操作
    int* diff = (int*)calloc(ASize + 1, sizeof(int));
    int flips = 0; // 当前翻转次数
    int result = 0; // 总翻转次数
    
    for (int i = 0; i < ASize; i++) {
        // 通过差分数组计算当前位置的实际翻转次数
        flips += diff[i];
        
        // 判断当前位置的实际值
        // 如果A[i] + flips是偶数，说明实际值为0，需要翻转
        if ((A[i] + flips) % 2 == 0) {
            // 检查是否有足够的元素进行K长度的翻转
            if (i + K > ASize) {
                free(diff);
                return -1;
            }
            
            // 记录翻转操作
            diff[i] += 1;
            diff[i + K] -= 1;
            flips += 1;
            result += 1;
        }
    }
    
    free(diff);
    return result;
}

// 算法核心思想：
// 1. 使用差分数组记录翻转操作的影响
// 2. 通过前缀和计算当前位置的实际翻转次数
// 3. 贪心地进行翻转操作

// 时间复杂度分析：
// - 遍历数组：O(n)
// - 差分数组操作：O(1)
// - 总体时间复杂度：O(n)
// - 空间复杂度：O(n)
*/

// 算法应用场景：
// 1. 位操作问题
// 2. 贪心算法应用
// 3. 差分数组优化