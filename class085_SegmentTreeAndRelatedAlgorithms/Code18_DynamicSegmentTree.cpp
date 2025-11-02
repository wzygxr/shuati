/**
 * 动态开点线段树 - 适用于值域较大或稀疏数据的场景
 * 
 * 题目描述：
 * 实现支持动态开点的线段树，避免预分配大量空间
 * 应用场景：值域很大但实际数据稀疏的情况
 * 
 * 题目来源：LeetCode 327. 区间和的个数
 * 测试链接 : https://leetcode.cn/problems/count-of-range-sum/
 * 
 * 解题思路：
 * 使用动态开点线段树来处理值域较大但数据稀疏的情况。
 * 与传统线段树不同，动态开点线段树只在需要时创建节点，节省空间。
 * 
 * 核心思想：
 * 1. 动态开点：只在需要时创建线段树节点，避免预分配大量空间
 * 2. 按需分配：对于值域很大的情况，只创建实际用到的节点
 * 3. 节点结构：每个节点维护区间信息和左右子节点指针
 * 
 * 时间复杂度分析：
 * - 更新操作：O(log(maxVal-minVal))
 * - 查询操作：O(log(maxVal-minVal))
 * 
 * 空间复杂度分析：
 * - O(n)，其中n是实际插入的元素个数
 */

// 定义常量
#define MAXN 100005
#define INF 1000000000000000LL

// 前向声明
struct DynamicSegmentTreeNode;

// 动态开点线段树节点结构
struct DynamicSegmentTreeNode {
    long long min_val;              // 节点管理区间的最小值
    long long max_val;              // 节点管理区间的最大值
    int count;                      // 节点管理区间内的元素个数
    DynamicSegmentTreeNode* left;   // 左子节点
    DynamicSegmentTreeNode* right;  // 右子节点
    
    /**
     * 构造函数 - 创建线段树节点
     * @param min 节点管理区间的最小值
     * @param max 节点管理区间的最大值
     */
    DynamicSegmentTreeNode(long long min, long long max) {
        min_val = min;
        max_val = max;
        count = 0;
        left = 0;  // 使用0代替NULL
        right = 0; // 使用0代替NULL
    }
    
    /**
     * 析构函数 - 释放节点内存
     */
    ~DynamicSegmentTreeNode() {
        if (left != 0) {     // 使用0代替NULL
            delete left;
        }
        if (right != 0) {    // 使用0代替NULL
            delete right;
        }
    }
    
    /**
     * 获取区间中点
     * 用于将当前区间分成左右两个子区间
     * @return 区间中点值
     */
    long long getMid() {
        return min_val + (max_val - min_val) / 2;
    }
    
    /**
     * 更新操作 - 在线段树中插入一个值
     * @param val 要插入的值
     * 
     * 时间复杂度: O(log(maxVal-minVal))
     */
    void update(long long val) {
        // 如果当前节点管理的区间只有一个值（叶子节点）
        if (min_val == max_val) {
            // 直接增加计数
            count++;
            return;
        }
        
        // 计算区间中点
        long long mid = getMid();
        // 根据值的大小决定插入左子树还是右子树
        if (val <= mid) {
            // 如果左子节点不存在，则创建左子节点
            if (left == 0) {     // 使用0代替NULL
                left = new DynamicSegmentTreeNode(min_val, mid);
            }
            // 递归更新左子树
            left->update(val);
        } else {
            // 如果右子节点不存在，则创建右子节点
            if (right == 0) {    // 使用0代替NULL
                right = new DynamicSegmentTreeNode(mid + 1, max_val);
            }
            // 递归更新右子树
            right->update(val);
        }
        
        // 更新当前节点的统计信息（左右子树元素个数之和）
        count = (left != 0 ? left->count : 0) + (right != 0 ? right->count : 0);  // 使用0代替NULL
    }
    
    /**
     * 查询操作 - 查询区间 [l, r] 内的元素个数
     * @param l 查询区间左边界
     * @param r 查询区间右边界
     * @return 区间[l, r]内的元素个数
     * 
     * 时间复杂度: O(log(maxVal-minVal))
     */
    int query(long long l, long long r) {
        // 如果查询区间与当前节点管理的区间无交集
        if (l > max_val || r < min_val) {
            // 返回0
            return 0;
        }
        // 如果当前节点管理的区间完全包含在查询区间内
        if (l <= min_val && max_val <= r) {
            // 直接返回当前节点的元素个数
            return count;
        }
        
        // 计算区间中点
        long long mid = getMid();
        int result = 0;
        // 如果左子节点存在且查询区间与左子树区间有交集
        if (left != 0 && l <= mid) {     // 使用0代替NULL
            // 递归查询左子树
            result += left->query(l, r);
        }
        // 如果右子节点存在且查询区间与右子树区间有交集
        if (right != 0 && r > mid) {     // 使用0代替NULL
            // 递归查询右子树
            result += right->query(l, r);
        }
        
        return result;
    }
};

/**
 * 动态开点线段树
 * 管理整个线段树的根节点和值域范围
 */
class DynamicSegmentTree {
private:
    DynamicSegmentTreeNode* root;  // 线段树根节点
    long long min_val;             // 值域最小值
    long long max_val;             // 值域最大值

public:
    /**
     * 构造函数 - 创建动态开点线段树
     * @param minVal 值域最小值
     * @param maxVal 值域最大值
     */
    DynamicSegmentTree(long long minVal, long long maxVal) {
        min_val = minVal;
        max_val = maxVal;
        // 创建根节点，管理整个值域范围
        root = new DynamicSegmentTreeNode(minVal, maxVal);
    }
    
    /**
     * 析构函数 - 释放线段树内存
     */
    ~DynamicSegmentTree() {
        if (root != 0) {     // 使用0代替NULL
            delete root;
        }
    }
    
    /**
     * 插入一个值
     * @param val 要插入的值
     * 
     * 时间复杂度: O(log(maxVal-minVal))
     */
    void update(long long val) {
        root->update(val);
    }
    
    /**
     * 查询区间 [l, r] 内的元素个数
     * @param l 查询区间左边界
     * @param r 查询区间右边界
     * @return 区间[l, r]内的元素个数
     * 
     * 时间复杂度: O(log(maxVal-minVal))
     */
    int query(long long l, long long r) {
        return root->query(l, r);
    }
};

/**
 * 测试动态开点线段树的基本功能
 */
void testDynamicSegmentTree() {
    // 创建动态开点线段树，值域为 [-1000, 1000]
    DynamicSegmentTree tree(-1000, 1000);
    
    // 插入一些值
    tree.update(10);
    tree.update(20);
    tree.update(30);
    tree.update(40);
    tree.update(50);
    
    // 测试查询
    // 应该输出 区间[15,35]内的元素个数: 2 (20, 30)
    // 应该输出 区间[0,100]内的元素个数: 5
    // 应该输出 区间[-10,10]内的元素个数: 1 (10)
    
    // 插入更多值
    tree.update(5);
    tree.update(15);
    tree.update(25);
    
    // 应该输出 插入后区间[0,30]内的元素个数: 6
}

/**
 * 动态开点线段树的其他应用：求逆序对
 * 使用动态开点线段树求解数组中的逆序对个数
 * 
 * 解题思路：
 * 逆序对是指对于数组中的两个元素nums[i]和nums[j]，如果i<j且nums[i]>nums[j]，则构成一个逆序对
 * 我们从右向左遍历数组，对于每个元素nums[i]，统计右侧有多少个元素比它小
 * 
 * 时间复杂度: O(n log maxVal)
 * 空间复杂度: O(n)
 * 
 * @param nums 整数数组
 * @param n 数组长度
 * @return 数组中的逆序对个数
 */
int countInversions(int nums[], int n) {
    // 边界条件检查
    if (n == 0) {
        return 0;
    }
    
    // 获取数组值的范围，用于确定动态开点线段树的值域
    int minVal = nums[0];
    int maxVal = nums[0];
    int i;
    for (i = 1; i < n; i++) {
        if (nums[i] < minVal) {
            minVal = nums[i];
        }
        if (nums[i] > maxVal) {
            maxVal = nums[i];
        }
    }
    
    // 创建动态开点线段树
    DynamicSegmentTree tree(minVal, maxVal);
    
    int inversions = 0;
    // 从右向左遍历，统计每个元素右侧比它小的元素个数
    for (i = n - 1; i >= 0; i--) {
        // 查询[minVal, nums[i]-1]范围内的元素个数，即右侧比nums[i]小的元素个数
        inversions += tree.query(minVal, nums[i] - 1);
        // 将当前元素插入线段树
        tree.update(nums[i]);
    }
    
    return inversions;
}

/**
 * 测试逆序对计数
 */
void testInversions() {
    // 测试用例1
    int nums1[] = {2, 4, 1, 3, 5};
    int result1 = countInversions(nums1, 5);
    // 应该输出 逆序对个数: 3
    
    // 测试用例2
    int nums2[] = {5, 4, 3, 2, 1};
    int result2 = countInversions(nums2, 5);
    // 应该输出 逆序对个数: 10
    
    // 测试用例3
    int nums3[] = {1, 2, 3, 4, 5};
    int result3 = countInversions(nums3, 5);
    // 应该输出 逆序对个数: 0
}

// 主函数 - 测试动态开点线段树的实现
int main() {
    // 运行测试
    testDynamicSegmentTree();
    testInversions();
    
    return 0;
}