#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <cmath>
using namespace std;

/**
 * LeetCode 1814 数对统计问题的普通莫队算法实现
 * 
 * 题目描述：
 * 统计数组中满足 nums[i] + reverse(nums[j]) == nums[j] + reverse(nums[i]) 的数对 (i, j) 的个数，其中 0 <= i < j < n
 * 
 * 解题思路：
 * 1. 首先观察等式 nums[i] + reverse(nums[j]) == nums[j] + reverse(nums[i])
 * 2. 可以变形为 nums[i] - reverse(nums[i]) == nums[j] - reverse(nums[j])
 * 3. 令 a[i] = nums[i] - reverse(nums[i])，则问题转化为统计有多少对 (i, j) 满足 a[i] == a[j] 且 i < j
 * 4. 这样我们可以将问题转化为区间查询问题，使用莫队算法来优化计算
 * 
 * 时间复杂度分析：
 * - 莫队算法的时间复杂度为 O((n + q) * sqrt(n))，其中 n 是数组长度，q 是查询次数
 * - 在本题中，我们可以看作是一个离线查询，所以时间复杂度为 O(n * sqrt(n))
 * 
 * 空间复杂度分析：
 * - 存储数组、a数组、查询结构等需要 O(n) 的空间
 * - 统计频率的哈希表需要 O(n) 的空间
 * - 总体空间复杂度为 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理数组为空的情况
 * 2. 性能优化：使用快速输入输出，预处理所有reverse值
 * 3. 代码可读性：清晰的变量命名和详细的注释
 * 4. 数据类型：使用long long避免溢出
 */

// 用于存储查询的结构
struct Query {
    int l;  // 查询的左边界
    int r;  // 查询的右边界
    int idx;  // 查询的索引，用于输出答案时保持顺序
    
    Query(int l, int r, int idx) : l(l), r(r), idx(idx) {}
};

// 数组的原始值
vector<int> nums;
// 存储nums[i] - reverse(nums[i])的值
vector<long long> a;
// 块的大小
int blockSize;
// 用于存储每个a[i]出现的频率
unordered_map<long long, int> frequencyMap;
// 当前满足条件的数对数量
long long currentResult;

/**
 * 计算一个数的反转
 * @param num 输入的整数
 * @return 反转后的整数
 */
int reverseNumber(int num) {
    int reversed = 0;
    while (num > 0) {
        reversed = reversed * 10 + num % 10;
        num /= 10;
    }
    return reversed;
}

/**
 * 比较两个查询的顺序，用于莫队算法的排序
 * @param q1 第一个查询
 * @param q2 第二个查询
 * @return 比较结果
 */
bool compareQueries(const Query& q1, const Query& q2) {
    // 首先按照左边界所在的块排序
    if (q1.l / blockSize != q2.l / blockSize) {
        return q1.l / blockSize < q2.l / blockSize;
    }
    // 对于同一块内的查询，按照右边界排序，偶数块升序，奇数块降序（奇偶排序优化）
    if ((q1.l / blockSize) % 2 == 0) {
        return q1.r < q2.r;
    } else {
        return q1.r > q2.r;
    }
}

/**
 * 添加一个元素到当前区间
 * @param pos 元素的位置
 */
void add(int pos) {
    long long val = a[pos];
    // 如果这个值之前已经出现过，那么新增的这个元素会与之前的所有相同值形成新的数对
    currentResult += frequencyMap[val];
    // 更新频率
    frequencyMap[val]++;
}

/**
 * 从当前区间移除一个元素
 * @param pos 元素的位置
 */
void remove(int pos) {
    long long val = a[pos];
    // 先减少频率，再更新结果
    frequencyMap[val]--;
    // 移除的元素会减少的数对数量等于移除前该值的频率-1（因为它不再与其他相同值形成数对）
    currentResult -= frequencyMap[val];
}

/**
 * 主解题函数
 * @param nums 输入数组
 * @return 满足条件的数对数量
 */
int countNicePairs(vector<int>& numsInput) {
    nums = numsInput;
    // 异常处理：空数组或单元素数组没有数对
    if (nums.size() <= 1) {
        return 0;
    }
    
    int n = nums.size();
    
    // 预处理a数组
    a.resize(n);
    for (int i = 0; i < n; i++) {
        a[i] = (long long)nums[i] - reverseNumber(nums[i]);
    }
    
    // 创建一个查询，查询整个数组
    vector<Query> queries;
    queries.emplace_back(0, n - 1, 0);
    
    // 计算块的大小，一般取sqrt(n)左右
    blockSize = static_cast<int>(sqrt(n)) + 1;
    
    // 对查询进行排序
    sort(queries.begin(), queries.end(), compareQueries);
    
    // 初始化
    frequencyMap.clear();
    currentResult = 0;
    vector<long long> results(1, 0);
    const int MOD = 1e9 + 7;
    
    // 初始化当前区间的左右指针
    int curL = 0;
    int curR = -1;
    
    // 处理每个查询
    for (const auto& q : queries) {
        // 调整左右指针到目标位置
        while (curL > q.l) add(--curL);
        while (curR < q.r) add(++curR);
        while (curL < q.l) remove(curL++);
        while (curR > q.r) remove(curR--);
        
        // 保存当前查询的结果
        results[q.idx] = currentResult % MOD;
    }
    
    return static_cast<int>(results[0]);
}

/**
 * 主函数，用于测试
 */
int main() {
    // 测试用例1
    vector<int> nums1 = {42, 11, 1, 97};
    cout << "Test Case 1: " << countNicePairs(nums1) << endl;  // 预期输出: 2
    
    // 测试用例2
    vector<int> nums2 = {13, 10, 35, 24, 76};
    cout << "Test Case 2: " << countNicePairs(nums2) << endl;  // 预期输出: 4
    
    // 边界测试用例
    vector<int> nums3 = {1};
    cout << "Test Case 3 (Single element): " << countNicePairs(nums3) << endl;  // 预期输出: 0
    
    vector<int> nums4 = {};
    cout << "Test Case 4 (Empty array): " << countNicePairs(nums4) << endl;  // 预期输出: 0
    
    return 0;
}