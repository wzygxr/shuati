// 315. 计算右侧小于当前元素的个数
// 平台: LeetCode
// 难度: 困难
// 标签: CDQ分治, 分治
// 链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
// 
// 题目描述:
// 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
// counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
// 
// 示例:
// 输入: nums = [5,2,6,1]
// 输出: [2,1,1,0]
// 解释:
// 5 的右侧有 2 个更小的元素 (2 和 1)
// 2 的右侧有 1 个更小的元素 (1)
// 6 的右侧有 1 个更小的元素 (1)
// 1 的右侧有 0 个更小的元素
// 
// 解题思路:
// 使用CDQ分治解决这个问题，将问题转化为三维偏序问题：
// 1. 第一维：索引，表示元素在原数组中的位置
// 2. 第二维：数值，表示元素的值
// 3. 第三维：时间/操作类型，用于区分查询和更新操作
// 
// 我们将每个元素看作两种操作：
// 1. 更新操作：在位置i插入数值nums[i]
// 2. 查询操作：查询在位置i右侧小于nums[i]的元素个数
// 
// 为了方便处理，我们从右向左遍历数组，这样问题就转化为：
// 对于每个元素，查询在它左侧（即原数组中它右侧）已经插入的小于它的元素个数
// 
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const int MAXN = 100005;

// 定义操作结构体
// op: 操作类型，1表示插入，-1表示查询
// val: 元素值
// idx: 原始索引
// id: 操作编号
struct Operation {
    int op, val, idx, id;
};

int cmp_operation(const void* a, const void* b) {
    struct Operation* x = (struct Operation*)a;
    struct Operation* y = (struct Operation*)b;
    if (x->val != y->val) return x->val - y->val;
    return y->op - x->op;  // 查询操作优先于插入操作
}

struct Operation ops[2 * MAXN];
struct Operation tmp[2 * MAXN];

int n;
int result[MAXN];
int bit[MAXN];  // 树状数组
int sorted_nums[MAXN];

// 树状数组操作
int lowbit(int x) {
    return x & (-x);
}

void add(int x, int v) {
    for (int i = x; i <= n; i += lowbit(i)) {
        bit[i] += v;
    }
}

int query(int x) {
    int res = 0;
    for (int i = x; i > 0; i -= lowbit(i)) {
        res += bit[i];
    }
    return res;
}

// CDQ分治主函数
void cdq(int l, int r) {
    if (l >= r) return;
    
    int mid = (l + r) >> 1;
    cdq(l, mid);
    cdq(mid + 1, r);
    
    // 合并过程，计算左半部分对右半部分的贡献
    int i = l, j = mid + 1, k = l;
    while (i <= mid && j <= r) {
        if (ops[i].idx <= ops[j].idx) {
            // 左半部分的元素位置小于等于右半部分，处理插入操作
            if (ops[i].op == 1) {
                add(ops[i].id, ops[i].op);  // 插入元素
            }
            tmp[k++] = ops[i++];
        } else {
            // 右半部分的元素位置更大，处理查询操作
            if (ops[j].op == -1) {
                // 查询小于当前值的元素个数
                result[ops[j].id] += query(ops[j].id - 1);
            }
            tmp[k++] = ops[j++];
        }
    }
    
    // 处理剩余元素
    while (i <= mid) {
        tmp[k++] = ops[i++];
    }
    while (j <= r) {
        if (ops[j].op == -1) {
            result[ops[j].id] += query(ops[j].id - 1);
        }
        tmp[k++] = ops[j++];
    }
    
    // 清理树状数组
    for (int t = l; t <= mid; t++) {
        if (ops[t].op == 1) {
            add(ops[t].id, -ops[t].op);
        }
    }
    
    // 将临时数组内容复制回原数组
    for (int t = l; t <= r; t++) {
        ops[t] = tmp[t];
    }
}

// 离散化函数
int discretize(int nums[], int size) {
    memcpy(sorted_nums, nums, sizeof(int) * size);
    
    // 手动排序
    for (int i = 0; i < size - 1; i++) {
        for (int j = 0; j < size - 1 - i; j++) {
            if (sorted_nums[j] > sorted_nums[j + 1]) {
                int temp = sorted_nums[j];
                sorted_nums[j] = sorted_nums[j + 1];
                sorted_nums[j + 1] = temp;
            }
        }
    }
    
    // 去重
    int unique_size = 1;
    for (int i = 1; i < size; i++) {
        if (sorted_nums[i] != sorted_nums[unique_size - 1]) {
            sorted_nums[unique_size++] = sorted_nums[i];
        }
    }
    
    return unique_size;
}

// 查找离散化后的值
int find_id(int val, int size) {
    // 二分查找
    int left = 0, right = size - 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (sorted_nums[mid] >= val) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return left + 1;
}

int* countSmaller(int nums[], int size, int* returnSize) {
    n = size;
    *returnSize = size;
    if (n == 0) return NULL;
    
    // 离散化处理
    int unique_size = discretize(nums, size);
    
    int cnt = 0;
    // 从右向左处理，构造操作序列
    for (int i = n - 1; i >= 0; i--) {
        int val_id = find_id(nums[i], unique_size);
        
        // 插入操作
        ops[++cnt].op = 1;
        ops[cnt].val = nums[i];
        ops[cnt].idx = i;
        ops[cnt].id = val_id;
        
        // 查询操作
        ops[++cnt].op = -1;
        ops[cnt].val = nums[i] - 1;
        ops[cnt].idx = i;
        ops[cnt].id = val_id;  // 查询小于nums[i]的元素个数
    }
    
    // 按值排序
    qsort(ops + 1, cnt, sizeof(struct Operation), cmp_operation);
    
    // 初始化结果数组和树状数组
    memset(result, 0, sizeof(result));
    memset(bit, 0, sizeof(bit));
    
    // 执行CDQ分治
    cdq(1, cnt);
    
    // 构造结果
    int* res = (int*)malloc(sizeof(int) * n);
    for (int i = 0; i < n; i++) {
        res[i] = result[i];
    }
    return res;
}

int main() {
    // 测试用例
    int nums1[] = {5, 2, 6, 1};
    int returnSize;
    int* result1 = countSmaller(nums1, 4, &returnSize);
    
    printf("输入: [5,2,6,1]\n");
    printf("输出: [");
    for (int i = 0; i < returnSize; i++) {
        printf("%d", result1[i]);
        if (i < returnSize - 1) printf(",");
    }
    printf("]\n");
    printf("期望: [2,1,1,0]\n");
    
    free(result1);
    return 0;
}

// C++版本的解决方案 - CDQ分治法解决右侧元素统计问题
#include <bits/stdc++.h>
using namespace std;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数
 * 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 * 
 * 解题思路：
 * 使用CDQ分治法结合树状数组解决此问题。将问题转化为三维偏序问题：
 * 1. 第一维：索引，表示元素在原数组中的位置（保证时序性）
 * 2. 第二维：数值，表示元素的值
 * 3. 第三维：操作类型，区分插入和查询操作
 * 
 * 时间复杂度：O(n log²n) - CDQ分治的时间复杂度
 * 空间复杂度：O(n) - 存储操作数组、结果数组和树状数组
 */
class Solution {
public:
    /**
     * 计算右侧小于当前元素的个数的主函数
     * @param nums 输入数组
     * @return 结果数组，counts[i]表示nums[i]右侧小于nums[i]的元素个数
     */
    vector<int> countSmaller(vector<int>& nums) {
        int n = nums.size();
        // 处理边界情况：空数组
        if (n == 0) return {};
        
        /**
         * 离散化处理：将原始数值映射到较小的连续整数范围
         * 这一步对于树状数组的实现至关重要，可以节省空间并提高效率
         */
        vector<int> sorted_nums = nums;
        sort(sorted_nums.begin(), sorted_nums.end());
        sorted_nums.erase(unique(sorted_nums.begin(), sorted_nums.end()), sorted_nums.end());
        
        /**
         * 定义操作结构体：
         * - op: 操作类型，1表示插入操作，-1表示查询操作
         * - val: 元素的原始值
         * - idx: 元素在原始数组中的索引位置
         * - id: 离散化后的数值标识
         */
        struct Operation {
            int op, val, idx, id;
            
            // 定义操作的比较规则
            bool operator<(const Operation& other) const {
                // 首先按照元素值排序
                if (val != other.val) return val < other.val;
                // 值相同时，查询操作优先于插入操作，避免重复计数
                return op > other.op;
            }
        };
        
        vector<Operation> ops;          // 存储所有操作
        vector<int> result(n, 0);       // 结果数组
        vector<int> bit(n + 1, 0);      // 树状数组，用于高效查询前缀和
        
        /**
         * 树状数组的三个基本操作：
         * 1. lowbit：获取x的最低位1
         * 2. add：在指定位置增加一个值
         * 3. query：查询前缀和
         */
        auto lowbit = [](int x) {
            return x & (-x);
        };
        
        auto add = [&](int x, int v) {
            for (int i = x; i <= n; i += lowbit(i)) {
                bit[i] += v;
            }
        };
        
        auto query = [&](int x) {
            int res = 0;
            for (int i = x; i > 0; i -= lowbit(i)) {
                res += bit[i];
            }
            return res;
        };
        
        /**
         * CDQ分治主函数：
         * @param l 操作区间的左端点
         * @param r 操作区间的右端点
         * 
         * 核心思想：将操作序列分成左右两半，递归处理每一半，
         * 然后合并时处理跨越左右两部分的操作对结果的影响
         */
        function<void(int, int)> cdq = [&](int l, int r) {
            // 递归终止条件：区间长度为0或1
            if (l >= r) return;
            
            // 划分子区间
            int mid = (l + r) >> 1;  // 等同于(l + r) / 2，但位运算效率更高
            
            // 递归处理左右子区间
            cdq(l, mid);
            cdq(mid + 1, r);
            
            // 合并阶段：计算左半部分对右半部分的贡献
            vector<Operation> tmp(r - l + 1);  // 临时数组用于归并排序
            int i = l;      // 左半部分指针
            int j = mid + 1; // 右半部分指针
            int k = 0;      // 临时数组指针
            
            // 合并两个有序子区间，同时计算贡献
            while (i <= mid && j <= r) {
                if (ops[i].idx <= ops[j].idx) {
                    // 左半部分的操作先于右半部分发生，处理插入操作
                    if (ops[i].op == 1) {
                        add(ops[i].id, ops[i].op);  // 在树状数组中插入元素
                    }
                    tmp[k++] = ops[i++];
                } else {
                    // 右半部分的操作先于左半部分发生，处理查询操作
                    if (ops[j].op == -1) {
                        // 查询树状数组中小于当前值的元素个数
                        result[ops[j].id] += query(ops[j].id - 1);
                    }
                    tmp[k++] = ops[j++];
                }
            }
            
            // 处理左半部分剩余的操作
            while (i <= mid) {
                tmp[k++] = ops[i++];
            }
            
            // 处理右半部分剩余的操作
            while (j <= r) {
                if (ops[j].op == -1) {
                    result[ops[j].id] += query(ops[j].id - 1);
                }
                tmp[k++] = ops[j++];
            }
            
            // 清理树状数组，移除左半部分的插入操作影响
            for (int t = l; t <= mid; t++) {
                if (ops[t].op == 1) {
                    add(ops[t].id, -ops[t].op);
                }
            }
            
            // 将临时数组内容复制回原数组，保证区间有序
            for (int t = 0; t < k; t++) {
                ops[l + t] = tmp[t];
            }
        };
        
        /**
         * 构造操作序列：
         * - 从右向左遍历原数组
         * - 对每个元素，生成一个插入操作和一个查询操作
         * - 这样，查询操作会统计在它之前（原数组中在它之后）插入的小于它的元素
         */
        for (int i = n - 1; i >= 0; i--) {
            // 获取离散化后的值
            int val_id = lower_bound(sorted_nums.begin(), sorted_nums.end(), nums[i]) - sorted_nums.begin() + 1;
            
            // 插入操作：将当前元素插入树状数组
            ops.push_back({1, nums[i], i, val_id});
            
            // 查询操作：查询小于当前元素的已插入元素个数
            ops.push_back({-1, nums[i] - 1, i, val_id});
        }
        
        // 按照操作值和类型进行排序
        sort(ops.begin(), ops.end());
        
        // 执行CDQ分治算法
        cdq(0, ops.size() - 1);
        
        return result;
    }
};

/**
 * 主函数：测试Solution类的实现
 */
int main() {
    Solution solution;
    
    // 测试用例
    vector<int> nums1 = {5, 2, 6, 1};
    vector<int> result1 = solution.countSmaller(nums1);
    
    cout << "输入: [5,2,6,1]" << endl;
    cout << "输出: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    cout << "期望: [2,1,1,0]" << endl;
    
    // 额外测试用例：空数组
    vector<int> empty_nums = {};
    vector<int> empty_result = solution.countSmaller(empty_nums);
    cout << "\n空数组测试:" << endl;
    cout << "输出大小: " << empty_result.size() << endl;
    
    // 额外测试用例：全相同元素
    vector<int> same_nums = {3, 3, 3, 3};
    vector<int> same_result = solution.countSmaller(same_nums);
    cout << "\n全相同元素测试:" << endl;
    cout << "输出: [";
    for (int i = 0; i < same_result.size(); i++) {
        cout << same_result[i];
        if (i < same_result.size() - 1) cout << ",";
    }
    cout << "]" << endl;
    
    return 0;
}
