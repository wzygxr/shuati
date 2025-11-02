#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 洛谷 P5356 由乃打扑克
 * 题目要求：区间查询第k小，区间加法
 * 核心技巧：分块排序 + 二分答案
 * 时间复杂度：O(√n * log n) / 查询，O(√n) / 修改
 * 空间复杂度：O(n)
 * 测试链接：https://www.luogu.com.cn/problem/P5356
 * 
 * 算法思想详解：
 * 1. 将数组分成大小为√n的块
 * 2. 对每个块维护一个排序后的副本，便于二分查找
 * 3. 对每个块维护一个加法标记（lazy标记）
 * 4. 区间加法操作：
 *    - 对于完整块，更新块的加法标记
 *    - 对于不完整块，暴力修改原始数组，并重新排序该块
 * 5. 区间第k小查询：
 *    - 对整个值域进行二分查找
 *    - 对于每个中间值mid，统计区间内小于等于mid的元素个数
 *    - 根据统计结果调整二分边界
 */

class BlockKth {
private:
    vector<int> arr;             // 原始数组
    vector<int> blockAdd;        // 每个块的加法标记
    int blockSize;               // 块的大小
    int blockCount;              // 块的数量
    vector<vector<int>> sortedBlocks; // 每个块的排序副本
    
    /**
     * 重建指定块的排序数组
     * @param blockId 块的索引
     */
    void rebuildBlock(int blockId) {
        int start = blockId * blockSize;
        int end = min((blockId + 1) * blockSize, (int)arr.size());
        sortedBlocks[blockId].clear();
        for (int i = start; i < end; ++i) {
            sortedBlocks[blockId].push_back(arr[i]);
        }
        sort(sortedBlocks[blockId].begin(), sortedBlocks[blockId].end());
    }
    
public:
    /**
     * 构造函数，初始化数据结构
     * @param array 输入数组
     */
    BlockKth(const vector<int>& array) {
        arr = array;
        int n = array.size();
        blockSize = static_cast<int>(sqrt(n)) + 1;
        blockCount = (n + blockSize - 1) / blockSize;
        blockAdd.resize(blockCount, 0);
        sortedBlocks.resize(blockCount);
        
        // 初始化每个块的排序副本
        for (int i = 0; i < blockCount; ++i) {
            int start = i * blockSize;
            int end = min((i + 1) * blockSize, n);
            for (int j = start; j < end; ++j) {
                sortedBlocks[i].push_back(arr[j]);
            }
            sort(sortedBlocks[i].begin(), sortedBlocks[i].end());
        }
    }
    
    /**
     * 区间加法操作
     * @param l 左边界（包含，0-based）
     * @param r 右边界（包含，0-based）
     * @param val 要加的值
     */
    void addRange(int l, int r, int val) {
        int leftBlock = l / blockSize;
        int rightBlock = r / blockSize;
        
        // 如果在同一个块内，直接暴力修改
        if (leftBlock == rightBlock) {
            // 先处理块标记
            for (int i = l; i <= r; ++i) {
                arr[i] += val;
            }
            // 重新排序该块
            rebuildBlock(leftBlock);
            return;
        }
        
        // 处理左边不完整块
        for (int i = l; i < (leftBlock + 1) * blockSize; ++i) {
            arr[i] += val;
        }
        rebuildBlock(leftBlock);
        
        // 处理中间的完整块
        for (int i = leftBlock + 1; i < rightBlock; ++i) {
            blockAdd[i] += val;
        }
        
        // 处理右边不完整块
        for (int i = rightBlock * blockSize; i <= r; ++i) {
            arr[i] += val;
        }
        rebuildBlock(rightBlock);
    }
    
    /**
     * 统计区间[l,r]内小于等于x的元素个数
     * @param l 左边界
     * @param r 右边界
     * @param x 目标值
     * @return 元素个数
     */
    int countLeq(int l, int r, int x) {
        int leftBlock = l / blockSize;
        int rightBlock = r / blockSize;
        int count = 0;
        
        // 如果在同一个块内，直接暴力统计
        if (leftBlock == rightBlock) {
            for (int i = l; i <= r; ++i) {
                if (arr[i] + blockAdd[leftBlock] <= x) {
                    ++count;
                }
            }
            return count;
        }
        
        // 统计左边不完整块
        for (int i = l; i < (leftBlock + 1) * blockSize; ++i) {
            if (arr[i] + blockAdd[leftBlock] <= x) {
                ++count;
            }
        }
        
        // 统计中间的完整块
        for (int i = leftBlock + 1; i < rightBlock; ++i) {
            // 在排序后的块中二分查找x - blockAdd[i]
            int target = x - blockAdd[i];
            // 使用upper_bound找到第一个大于target的位置
            auto& block = sortedBlocks[i];
            auto it = upper_bound(block.begin(), block.end(), target);
            count += it - block.begin();
        }
        
        // 统计右边不完整块
        for (int i = rightBlock * blockSize; i <= r; ++i) {
            if (arr[i] + blockAdd[rightBlock] <= x) {
                ++count;
            }
        }
        
        return count;
    }
    
    /**
     * 查询区间[l,r]内的第k小元素
     * @param l 左边界（包含，0-based）
     * @param r 右边界（包含，0-based）
     * @param k 第k小（k>=1）
     * @return 第k小的元素值
     */
    int queryKth(int l, int r, int k) {
        // 优化：确定值域范围
        int minVal = INT_MAX;
        int maxVal = INT_MIN;
        
        // 方法1：遍历整个区间获取最值（O(n)时间，适合小区间）
        // 方法2：使用预处理的块最值（这里简化处理，直接遍历）
        for (int i = l; i <= r; ++i) {
            int blockId = i / blockSize;
            int val = arr[i] + blockAdd[blockId];
            minVal = min(minVal, val);
            maxVal = max(maxVal, val);
        }
        
        // 二分查找第k小的元素
        int left = minVal;
        int right = maxVal;
        while (left < right) {
            int mid = left + (right - left) / 2;
            int cnt = countLeq(l, r, mid);
            if (cnt >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
    
    /**
     * 获取指定位置的当前值
     * @param index 索引
     * @return 当前值
     */
    int getValue(int index) {
        int blockId = index / blockSize;
        return arr[index] + blockAdd[blockId];
    }
    
    /**
     * 完整查询优化版
     * 当数据规模很大时，使用更高效的值域估计
     */
    int queryKthOptimized(int l, int r, int k) {
        // 预估值域范围
        int left = 0;
        int right = 1e9; // 根据题目数据范围设置
        
        while (left < right) {
            int mid = left + (right - left) / 2;
            int cnt = countLeq(l, r, mid);
            if (cnt >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        
        return left;
    }
};

// 测试函数
void testBlockKth() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, m;
    cin >> n >> m;
    
    vector<int> array(n);
    for (int i = 0; i < n; ++i) {
        cin >> array[i];
    }
    
    BlockKth solution(array);
    
    while (m--) {
        int op;
        cin >> op;
        if (op == 1) {
            // 区间加法
            int l, r, val;
            cin >> l >> r >> val;
            l--; r--; // 转换为0-based索引
            solution.addRange(l, r, val);
        } else if (op == 2) {
            // 查询第k小
            int l, r, k;
            cin >> l >> r >> k;
            l--; r--; // 转换为0-based索引
            cout << solution.queryKth(l, r, k) << '\n';
        }
    }
}

// 性能测试函数
void performanceTest() {
    const int SIZE = 100000;
    vector<int> largeArray(SIZE);
    
    // 初始化数组
    for (int i = 0; i < SIZE; ++i) {
        largeArray[i] = rand() % 1000000;
    }
    
    BlockKth solution(largeArray);
    
    // 执行操作
    int ops = 1000;
    cout << "执行" << ops << "次随机操作...\n";
    
    for (int i = 0; i < ops; ++i) {
        if (rand() % 2 == 0) {
            // 区间加法
            int l = rand() % SIZE;
            int r = rand() % SIZE;
            if (l > r) swap(l, r);
            int val = rand() % 100;
            solution.addRange(l, r, val);
        } else {
            // 查询第k小
            int l = rand() % SIZE;
            int r = rand() % SIZE;
            if (l > r) swap(l, r);
            int k = rand() % (r - l + 1) + 1;
            if (i < 10) { // 只输出前10个查询结果
                int result = solution.queryKthOptimized(l, r, k);
                cout << "区间 [" << l << ", " << r << "] 的第" << k << "小是: " << result << '\n';
            }
        }
    }
    
    cout << "性能测试完成\n";
}

int main() {
    cout << "1. 标准测试（符合题目输入格式）" << endl;
    cout << "2. 性能测试" << endl;
    cout << "请选择测试类型：";
    
    int choice;
    cin >> choice;
    
    if (choice == 1) {
        testBlockKth();
    } else if (choice == 2) {
        performanceTest();
    }
    
    return 0;
}

/**
 * C++语言特定优化说明：
 * 1. 使用vector容器高效管理内存和数据
 * 2. 使用STL的upper_bound函数进行二分查找，性能更优
 * 3. 使用ios::sync_with_stdio(false)和cin.tie(0)加速输入输出
 * 4. 实现了优化版的查询函数，适用于大数据范围
 * 5. 使用内联函数和引用传递减少开销
 * 
 * 时间复杂度分析：
 * - 区间加法：O(√n)
 *   - 对于不完整块：需要O(√n)时间修改和排序
 *   - 对于完整块：O(1)时间更新标记
 * - 查询第k小：O(√n * log V)
 *   - 二分答案需要O(log V)次迭代
 *   - 每次迭代的countLeq操作需要O(√n)时间
 * 
 * 空间复杂度分析：
 * - O(n) 用于存储原始数组
 * - O(n) 用于存储排序后的块
 * - O(√n) 用于存储块标记
 * - 总体空间复杂度：O(n)
 * 
 * 边界情况处理：
 * 1. 空区间：在实际应用中需要检查
 * 2. 非法k值（k<=0或k>区间长度）：需要添加检查
 * 3. 大数据范围：使用queryKthOptimized处理
 */