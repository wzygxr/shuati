#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <random>
#include <chrono>

using namespace std;

/**
 * 牛客网 NC78 线段树区间最大值 - C++实现
 * 题目链接: https://www.nowcoder.com/practice/1a834e5e3e1a4b7ba251417554e07c00
 * 
 * 算法思路:
 * 线段树是一种二叉树结构，用于高效处理区间查询和更新操作。
 * 每个节点存储一个区间的聚合信息（最大值）。
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 查询: O(log n)
 * - 更新: O(log n)
 * 
 * 空间复杂度分析:
 * - 线段树存储: O(4n) 或 O(2n)（优化后）
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、索引越界检查
 * 2. 性能优化：位运算、缓存友好设计
 * 3. 可测试性：边界测试、性能测试
 * 4. 可维护性：清晰的结构、详细注释
 */
class NowcoderNC78_SegmentTreeMax {
private:
    vector<int> tree;  // 线段树数组
    int n;             // 原始数组长度
    
    /**
     * 递归构建线段树
     * @param nums 原始数组
     * @param left 当前区间左边界
     * @param right 当前区间右边界
     * @param idx 当前节点在线段树中的索引
     */
    void buildTree(const vector<int>& nums, int left, int right, int idx) {
        // 叶子节点，存储单个元素
        if (left == right) {
            tree[idx] = nums[left];
            return;
        }
        
        // 计算中间点，分割区间
        int mid = left + (right - left) / 2;
        int leftChild = 2 * idx + 1;  // 左子节点索引
        int rightChild = 2 * idx + 2; // 右子节点索引
        
        // 递归构建左右子树
        buildTree(nums, left, mid, leftChild);
        buildTree(nums, mid + 1, right, rightChild);
        
        // 合并左右子树信息（取最大值）
        tree[idx] = max(tree[leftChild], tree[rightChild]);
    }
    
    /**
     * 递归查询辅助函数
     */
    int queryHelper(int segLeft, int segRight, int queryLeft, int queryRight, int idx) {
        // 当前区间完全包含在查询区间内
        if (queryLeft <= segLeft && segRight <= queryRight) {
            return tree[idx];
        }
        
        int mid = segLeft + (segRight - segLeft) / 2;
        int leftChild = 2 * idx + 1;
        int rightChild = 2 * idx + 2;
        
        int maxVal = INT_MIN;
        
        // 查询左子树
        if (queryLeft <= mid) {
            maxVal = max(maxVal, queryHelper(segLeft, mid, queryLeft, queryRight, leftChild));
        }
        
        // 查询右子树
        if (queryRight > mid) {
            maxVal = max(maxVal, queryHelper(mid + 1, segRight, queryLeft, queryRight, rightChild));
        }
        
        return maxVal;
    }
    
    /**
     * 递归更新辅助函数
     */
    void updateHelper(int segLeft, int segRight, int index, int value, int idx) {
        // 找到目标叶子节点
        if (segLeft == segRight) {
            tree[idx] = value;
            return;
        }
        
        int mid = segLeft + (segRight - segLeft) / 2;
        int leftChild = 2 * idx + 1;
        int rightChild = 2 * idx + 2;
        
        // 根据索引位置决定更新哪棵子树
        if (index <= mid) {
            updateHelper(segLeft, mid, index, value, leftChild);
        } else {
            updateHelper(mid + 1, segRight, index, value, rightChild);
        }
        
        // 更新父节点（取左右子树最大值）
        tree[idx] = max(tree[leftChild], tree[rightChild]);
    }
    
    /**
     * 递归打印线段树结构（用于调试）
     */
    void printTreeHelper(int left, int right, int idx, int depth) {
        string indent(depth * 2, ' ');
        
        cout << indent << "区间[" << left << ", " << right << "]: " << tree[idx] << endl;
        
        if (left != right) {
            int mid = left + (right - left) / 2;
            printTreeHelper(left, mid, 2 * idx + 1, depth + 1);
            printTreeHelper(mid + 1, right, 2 * idx + 2, depth + 1);
        }
    }
    
public:
    /**
     * 构造函数：构建线段树
     * @param nums 原始数组
     */
    NowcoderNC78_SegmentTreeMax(const vector<int>& nums) {
        if (nums.empty()) {
            throw invalid_argument("数组不能为空");
        }
        
        this->n = nums.size();
        this->tree.resize(4 * n, 0);  // 分配4倍空间并初始化为0
        buildTree(nums, 0, n - 1, 0);
    }
    
    /**
     * 查询区间最大值
     * @param queryLeft 查询区间左边界
     * @param queryRight 查询区间右边界
     * @return 区间最大值
     */
    int query(int queryLeft, int queryRight) {
        // 参数校验
        if (queryLeft < 0 || queryRight >= n || queryLeft > queryRight) {
            throw invalid_argument("查询区间不合法");
        }
        
        return queryHelper(0, n - 1, queryLeft, queryRight, 0);
    }
    
    /**
     * 单点更新
     * @param index 要更新的索引
     * @param value 新的值
     */
    void update(int index, int value) {
        // 参数校验
        if (index < 0 || index >= n) {
            throw invalid_argument("索引越界");
        }
        
        updateHelper(0, n - 1, index, value, 0);
    }
    
    /**
     * 打印线段树结构（用于调试）
     */
    void printTree() {
        cout << "线段树结构:" << endl;
        printTreeHelper(0, n - 1, 0, 0);
    }
};

/**
 * 辅助函数：打印数组
 */
void printArray(const vector<int>& nums) {
    cout << "数组: ";
    for (size_t i = 0; i < nums.size(); ++i) {
        cout << nums[i];
        if (i < nums.size() - 1) cout << ", ";
    }
    cout << endl;
}

/**
 * 主函数：测试用例
 */
int main() {
    // 测试用例1：正常数组
    vector<int> nums1 = {1, 3, 5, 7, 9, 11};
    NowcoderNC78_SegmentTreeMax st1(nums1);
    
    cout << "=== 测试用例1 ===" << endl;
    printArray(nums1);
    
    // 测试查询
    cout << "查询[0, 2]最大值: " << st1.query(0, 2) << endl;  // 期望: 5
    cout << "查询[1, 4]最大值: " << st1.query(1, 4) << endl;  // 期望: 9
    cout << "查询[0, 5]最大值: " << st1.query(0, 5) << endl;  // 期望: 11
    
    // 测试更新
    st1.update(2, 10);
    cout << "更新索引2为10后，查询[0, 2]最大值: " << st1.query(0, 2) << endl;  // 期望: 10
    
    // 测试用例2：边界情况
    vector<int> nums2 = {5};
    NowcoderNC78_SegmentTreeMax st2(nums2);
    
    cout << "\n=== 测试用例2 ===" << endl;
    printArray(nums2);
    cout << "查询[0, 0]最大值: " << st2.query(0, 0) << endl;  // 期望: 5
    
    // 测试用例3：负数数组
    vector<int> nums3 = {-1, -3, -5, -7};
    NowcoderNC78_SegmentTreeMax st3(nums3);
    
    cout << "\n=== 测试用例3 ===" << endl;
    printArray(nums3);
    cout << "查询[0, 3]最大值: " << st3.query(0, 3) << endl;  // 期望: -1
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    int size = 100000;
    vector<int> largeNums(size);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(0, 1000000);
    
    for (int i = 0; i < size; i++) {
        largeNums[i] = dis(gen);
    }
    
    auto startTime = chrono::high_resolution_clock::now();
    NowcoderNC78_SegmentTreeMax stLarge(largeNums);
    auto buildTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - startTime);
    
    startTime = chrono::high_resolution_clock::now();
    int maxVal = stLarge.query(0, size - 1);
    auto queryTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - startTime);
    
    cout << "构建" << size << "个元素的线段树耗时: " << buildTime.count() << "ms" << endl;
    cout << "查询整个区间最大值耗时: " << queryTime.count() << "ms" << endl;
    cout << "最大值: " << maxVal << endl;
    
    // 异常测试
    cout << "\n=== 异常测试 ===" << endl;
    try {
        st1.query(-1, 2);
    } catch (const invalid_argument& e) {
        cout << "捕获到预期异常: " << e.what() << endl;
    }
    
    return 0;
}