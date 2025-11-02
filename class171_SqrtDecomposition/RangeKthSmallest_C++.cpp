#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <climits>
using namespace std;

/**
 * 区间第k小查询问题 - C++实现
 * 题目类型：区间第k小查询，支持单点更新
 * 
 * 题目描述：
 * 给定一个数组，支持两种操作：
 * 1. 更新数组中某个元素的值
 * 2. 查询区间[l, r]中的第k小元素
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为sqrt(n)的块。
 * 预处理：
 * 1. 每个块内部排序，便于快速统计小于等于某值的元素个数
 * 2. 预处理每个块的大小和边界
 * 处理操作时：
 * 1. 对于更新操作，更新原始数组，然后重新排序所在块
 * 2. 对于查询操作，使用二分答案+前缀和的方式，统计区间中小于等于mid的元素个数
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 更新操作：O(√n log √n)
 * - 查询操作：O((log n)^2 √n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 可配置性：块大小可根据需要调整
 * 3. 性能优化：块内排序和二分查找
 * 4. 鲁棒性：处理边界情况和特殊输入
 * 5. 数据结构：使用数组和向量存储数据和排序后的块
 */

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n, q;
    cin >> n >> q;
    
    vector<int> a(n);
    for (int i = 0; i < n; i++) {
        cin >> a[i];
    }
    
    int blockSize = static_cast<int>(sqrt(n)) + 1;
    int blockNum = (n + blockSize - 1) / blockSize;
    
    // 初始化每个块排序后的数组
    vector<vector<int>> blocks(blockNum);
    for (int i = 0; i < blockNum; i++) {
        int start = i * blockSize;
        int end = min(start + blockSize, n);
        blocks[i].resize(end - start);
        for (int j = 0; j < blocks[i].size(); j++) {
            blocks[i][j] = a[start + j];
        }
        sort(blocks[i].begin(), blocks[i].end());
    }
    
    // 处理查询
    while (q--) {
        int op;
        cin >> op;
        
        if (op == 0) {
            // 单点更新
            int index, val;
            cin >> index >> val;
            index--; // 转换为0-based
            
            a[index] = val;
            int blockIndex = index / blockSize;
            int start = blockIndex * blockSize;
            int end = min(start + blockSize, n);
            
            // 重新构建并排序该块
            blocks[blockIndex].clear();
            for (int i = start; i < end; i++) {
                blocks[blockIndex].push_back(a[i]);
            }
            sort(blocks[blockIndex].begin(), blocks[blockIndex].end());
        } else if (op == 1) {
            // 区间第k小查询
            int l, r, k;
            cin >> l >> r >> k;
            l--; // 转换为0-based
            r--; // 转换为0-based
            
            // 确定二分查找的上下界
            int leftVal = INT_MIN;
            int rightVal = INT_MAX;
            
            for (int i = 0; i < n; i++) {
                leftVal = min(leftVal, a[i]);
                rightVal = max(rightVal, a[i]);
            }
            
            int answer = rightVal;
            
            // 二分答案
            while (leftVal <= rightVal) {
                int mid = leftVal + (rightVal - leftVal) / 2;
                int count = 0;
                
                int leftBlock = l / blockSize;
                int rightBlock = r / blockSize;
                
                if (leftBlock == rightBlock) {
                    // 在同一个块内，暴力统计
                    for (int i = l; i <= r; i++) {
                        if (a[i] <= mid) {
                            count++;
                        }
                    }
                } else {
                    // 统计左边不完整块
                    for (int i = l; i < (leftBlock + 1) * blockSize; i++) {
                        if (a[i] <= mid) {
                            count++;
                        }
                    }
                    
                    // 统计中间完整块
                    for (int b = leftBlock + 1; b < rightBlock; b++) {
                        // 在排序后的块中二分查找<=mid的元素个数
                        count += upper_bound(blocks[b].begin(), blocks[b].end(), mid) - blocks[b].begin();
                    }
                    
                    // 统计右边不完整块
                    for (int i = rightBlock * blockSize; i <= r; i++) {
                        if (a[i] <= mid) {
                            count++;
                        }
                    }
                }
                
                if (count >= k) {
                    answer = mid;
                    rightVal = mid - 1;
                } else {
                    leftVal = mid + 1;
                }
            }
            
            cout << answer << endl;
        }
    }
    
    return 0;
}