#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <unordered_set>
#include <functional>
#include <algorithm>
#include <string>
#include <stdexcept>
#include <climits>
#include <cmath>
#include <sstream>
#include <iterator>
#include <map>
#include <set>
#include <stack>
#include <deque>
#include <list>

/**
 * 堆排序C++实现及相关题目
 * 
 * 本文件包含堆排序的基本实现以及多个经典堆相关题目的完整解法
 * 每个解法都包含详细的时间复杂度、空间复杂度分析和工程化考量
 * 
 * 作者: 算法之旅
 * 创建时间: 2024年
 * 版本: 1.0
 * 
 * 主要功能:
 * 1. 堆排序的两种实现方式
 * 2. 多个经典堆相关问题的C++解法
 * 3. 详细的注释和复杂度分析
 * 4. 工程化考量和异常处理
 * 
 * 题目来源平台:
 * - LeetCode (力扣): https://leetcode.cn/
 * - LintCode (炼码): https://www.lintcode.com/
 * - HackerRank: https://www.hackerrank.com/
 * - 洛谷 (Luogu): https://www.luogu.com.cn/
 * - AtCoder: https://atcoder.jp/
 * - 牛客网: https://www.nowcoder.com/
 * - CodeChef: https://www.codechef.com/
 * - SPOJ: https://www.spoj.com/
 * - Project Euler: https://projecteuler.net/
 * - HackerEarth: https://www.hackerearth.com/
 * - 计蒜客: https://www.jisuanke.com/
 * - USACO: http://usaco.org/
 * - UVa OJ: https://onlinejudge.org/
 * - Codeforces: https://codeforces.com/
 * - POJ: http://poj.org/
 * - HDU: http://acm.hdu.edu.cn/
 * - 剑指Offer: 面试经典题目
 * - 杭电 OJ: http://acm.hdu.edu.cn/
 * - LOJ: https://loj.ac/
 * - acwing: https://www.acwing.com/
 * - 赛码: https://www.acmcoder.com/
 * - zoj: http://acm.zju.edu.cn/
 * - MarsCode: https://www.marscode.cn/
 * - TimusOJ: http://acm.timus.ru/
 * - AizuOJ: http://judge.u-aizu.ac.jp/
 * - Comet OJ: https://www.cometoj.com/
 * - 杭州电子科技大学 OJ
 */

using namespace std;

class HeapSortSolution {
public:
    /*
     * 堆排序主函数
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(1)
     */
    static vector<int> sortArray(vector<int>& nums) {
        if (nums.size() <= 1) {
            return nums;
        }
        
        // heapSort2为从底到顶建堆然后排序
        heapSort2(nums);
        return nums;
    }

    // i位置的数，向上调整大根堆
    // arr[i] = x，x是新来的！往上看，直到不比父亲大，或者来到0位置(顶)
    static void heapInsert(vector<int>& arr, int i) {
        while (arr[i] > arr[(i - 1) / 2]) {
            swap(arr[i], arr[(i - 1) / 2]);
            i = (i - 1) / 2;
        }
    }

    // i位置的数，变小了，又想维持大根堆结构
    // 向下调整大根堆
    // 当前堆的大小为size
    static void heapify(vector<int>& arr, int i, int size) {
        int l = i * 2 + 1;
        while (l < size) {
            // 有左孩子，l
            // 右孩子，l+1
            // 评选，最强的孩子，是哪个下标的孩子
            int best = l + 1 < size && arr[l + 1] > arr[l] ? l + 1 : l;
            // 上面已经评选了最强的孩子，接下来，当前的数和最强的孩子之前，最强下标是谁
            best = arr[best] > arr[i] ? best : i;
            if (best == i) {
                break;
            }
            swap(arr[best], arr[i]);
            i = best;
            l = i * 2 + 1;
        }
    }

    // 从顶到底建立大根堆，O(n * logn)
    // 依次弹出堆内最大值并排好序，O(n * logn)
    // 整体时间复杂度O(n * logn)
    static void heapSort1(vector<int>& arr) {
        int n = arr.size();
        for (int i = 0; i < n; i++) {
            heapInsert(arr, i);
        }
        int size = n;
        while (size > 1) {
            swap(arr[0], arr[--size]);
            heapify(arr, 0, size);
        }
    }

    // 从底到顶建立大根堆，O(n)
    // 依次弹出堆内最大值并排好序，O(n * logn)
    // 整体时间复杂度O(n * logn)
    static void heapSort2(vector<int>& arr) {
        int n = arr.size();
        for (int i = n - 1; i >= 0; i--) {
            heapify(arr, i, n);
        }
        int size = n;
        while (size > 1) {
            swap(arr[0], arr[--size]);
            heapify(arr, 0, size);
        }
    }
    
    /*
     * 补充题目1: LeetCode 215. 数组中的第K个最大元素
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
     * 题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素
     * 
     * 解题思路:
     * 方法1: 使用堆排序完整排序后取第k个元素 - 时间复杂度 O(n log n)
     * 方法2: 使用大小为k的最小堆维护前k个最大元素 - 时间复杂度 O(n log k)
     * 方法3: 快速选择算法 - 平均时间复杂度 O(n)
     * 
     * 最优解: 快速选择算法，但这里展示堆的解法
     * 时间复杂度: O(n log k) - 遍历数组O(n)，每次堆操作O(log k)
     * 空间复杂度: O(k) - 堆的大小
     * 
     * 相关题目:
     * - 剑指Offer 40. 最小的k个数
     * - 牛客网 BM46 最小的K个数
     * - LintCode 461. Kth Smallest Numbers in Unsorted Array
     */
    static int findKthLargest(vector<int>& nums, int k) {
        // 使用最小堆维护前k个最大元素
        priority_queue<int, vector<int>, greater<int>> minHeap;
        
        for (int num : nums) {
            if (minHeap.size() < k) {
                minHeap.push(num);
            } else if (num > minHeap.top()) {
                minHeap.pop();
                minHeap.push(num);
            }
        }
        
        return minHeap.top();
    }
    
    /*
     * 补充题目2: LeetCode 347. 前 K 个高频元素
     * 链接: https://leetcode.cn/problems/top-k-frequent-elements/
     * 题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
     * 
     * 解题思路:
     * 1. 使用哈希表统计每个元素的频率 - 时间复杂度 O(n)
     * 2. 使用大小为k的最小堆维护前k个高频元素 - 时间复杂度 O(n log k)
     * 3. 遍历哈希表，维护堆的大小为k
     * 4. 从堆中取出元素即为结果
     * 
     * 时间复杂度: O(n log k) - n为数组长度
     * 空间复杂度: O(n + k) - 哈希表O(n)，堆O(k)
     * 
     * 是否最优解: 是，满足题目要求的复杂度优于O(n log n)
     * 
     * 相关题目:
     * - LeetCode 692. 前K个高频单词
     * - LintCode 1297. 统计右侧小于当前元素的个数
     */
    static vector<int> topKFrequent(vector<int>& nums, int k) {
        // 1. 统计频率
        unordered_map<int, int> freqMap;
        for (int num : nums) {
            freqMap[num]++;
        }
        
        // 2. 使用最小堆维护前k个高频元素
        // 比较依据是频率
        auto cmp = [&freqMap](int a, int b) {
            return freqMap[a] > freqMap[b]; // 最小堆
        };
        priority_queue<int, vector<int>, decltype(cmp)> minHeap(cmp);
        
        // 3. 遍历频率表，维护堆大小为k
        for (auto& pair : freqMap) {
            if (minHeap.size() < k) {
                minHeap.push(pair.first);
            } else if (freqMap[pair.first] > freqMap[minHeap.top()]) {
                minHeap.pop();
                minHeap.push(pair.first);
            }
        }
        
        // 4. 构造结果数组
        vector<int> result;
        while (!minHeap.empty()) {
            result.push_back(minHeap.top());
            minHeap.pop();
        }
        
        return result;
    }
    
    /*
     * 补充题目3: LeetCode 295. 数据流的中位数
     * 链接: https://leetcode.cn/problems/find-median-from-data-stream/
     * 题目描述: 中位数是有序整数列表中的中间值。如果列表的大小是偶数，则没有中间值，中位数是两个中间值的平均值
     * 
     * 解题思路:
     * 使用两个堆：
     * 1. 最大堆maxHeap存储较小的一半元素
     * 2. 最小堆minHeap存储较大的一半元素
     * 3. 保持两个堆的大小平衡（差值不超过1）
     * 
     * 时间复杂度: 
     * - 添加元素: O(log n) - 堆的插入和调整
     * - 查找中位数: O(1) - 直接访问堆顶
     * 空间复杂度: O(n) - 存储所有元素
     * 
     * 是否最优解: 是，这是处理动态中位数的经典解法
     * 
     * 相关题目:
     * - 剑指Offer 41. 数据流中的中位数
     * - HackerRank Find the Running Median
     * - 牛客网 NC134. 数据流中的中位数
     * - AtCoder ABC 127F - Absolute Minima
     */
    class MedianFinder {
    private:
        // 存储较小一半元素的最大堆
        priority_queue<int> maxHeap;
        // 存储较大一半元素的最小堆
        priority_queue<int, vector<int>, greater<int>> minHeap;
        
    public:
        MedianFinder() {}
        
        /*
         * 添加数字到数据结构中
         * 时间复杂度: O(log n)
         */
        void addNum(int num) {
            // 1. 根据num与两个堆堆顶的比较结果决定插入哪个堆
            if (maxHeap.empty() || num <= maxHeap.top()) {
                maxHeap.push(num);
            } else {
                minHeap.push(num);
            }
            
            // 2. 平衡两个堆的大小
            // 如果maxHeap比minHeap多2个元素，则移动一个元素到minHeap
            if (maxHeap.size() > minHeap.size() + 1) {
                minHeap.push(maxHeap.top());
                maxHeap.pop();
            }
            // 如果minHeap比maxHeap多1个元素，则移动一个元素到maxHeap
            else if (minHeap.size() > maxHeap.size() + 1) {
                maxHeap.push(minHeap.top());
                minHeap.pop();
            }
        }
        
        /*
         * 查找当前数据结构中的中位数
         * 时间复杂度: O(1)
         */
        double findMedian() {
            // 如果两个堆大小相等，返回两个堆顶的平均值
            if (maxHeap.size() == minHeap.size()) {
                return (maxHeap.top() + minHeap.top()) / 2.0;
            }
            // 如果maxHeap多一个元素，返回其堆顶
            else if (maxHeap.size() > minHeap.size()) {
                return maxHeap.top();
            }
            // 如果minHeap多一个元素，返回其堆顶
            else {
                return minHeap.top();
            }
        }
    };
    
    /*
     * 补充题目4: LeetCode 23. 合并K个升序链表
     * 链接: https://leetcode.cn/problems/merge-k-sorted-lists/
     * 题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中
     * 
     * 解题思路:
     * 使用最小堆维护K个链表的当前头节点，每次取出最小节点加入结果链表，
     * 并将该节点的下一个节点加入堆中
     * 
     * 时间复杂度: O(N log k) - N为所有节点总数，k为链表数量
     * 空间复杂度: O(k) - 堆的大小
     * 
     * 是否最优解: 是，这是合并K个有序链表的经典解法之一
     * 
     * 相关题目:
     * - LintCode 104. 合并k个排序链表
     * - 牛客网 NC51. 合并k个排序链表
     */
    struct ListNode {
        int val;
        ListNode *next;
        ListNode() : val(0), next(nullptr) {}
        ListNode(int x) : val(x), next(nullptr) {}
        ListNode(int x, ListNode *next) : val(x), next(next) {}
    };
    
    // 自定义比较函数对象
    struct Compare {
        bool operator()(ListNode* a, ListNode* b) {
            return a->val > b->val; // 最小堆
        }
    };
    
    static ListNode* mergeKLists(vector<ListNode*>& lists) {
        if (lists.empty()) {
            return nullptr;
        }
        
        // 使用最小堆维护K个链表的当前头节点
        priority_queue<ListNode*, vector<ListNode*>, Compare> minHeap;
        
        // 将所有非空链表的头节点加入堆中
        for (ListNode* list : lists) {
            if (list != nullptr) {
                minHeap.push(list);
            }
        }
        
        // 创建虚拟头节点
        ListNode dummy(0);
        ListNode* current = &dummy;
        
        // 当堆不为空时，不断取出最小节点
        while (!minHeap.empty()) {
            // 取出当前最小节点
            ListNode* node = minHeap.top();
            minHeap.pop();
            
            // 加入结果链表
            current->next = node;
            current = current->next;
            
            // 将该节点的下一个节点加入堆中（如果不为空）
            if (node->next != nullptr) {
                minHeap.push(node->next);
            }
        }
        
        return dummy.next;
    }
    
    /*
     * 补充题目5: LeetCode 703. 数据流的第K大元素
     * 链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
     * 题目描述: 设计一个找到数据流中第 k 大元素的类
     * 
     * 解题思路:
     * 使用大小为k的最小堆维护数据流中前k个最大元素
     * 堆顶即为第k大元素
     * 
     * 时间复杂度: 
     * - 初始化: O(n log k) - n为初始数组长度
     * - 添加元素: O(log k)
     * 空间复杂度: O(k) - 堆的大小
     * 
     * 是否最优解: 是，这是处理动态第K大元素的经典解法
     * 
     * 相关题目:
     * - 剑指Offer II 059. 数据流的第K大数值
     */
    class KthLargest {
    private:
        int k;
        priority_queue<int, vector<int>, greater<int>> minHeap;
        
    public:
        KthLargest(int k, vector<int>& nums) : k(k) {
            // 将初始数组中的元素加入堆中
            for (int num : nums) {
                add(num);
            }
        }
        
        /*
         * 向数据流中添加元素并返回当前第k大元素
         * 时间复杂度: O(log k)
         */
        int add(int val) {
            if (minHeap.size() < k) {
                minHeap.push(val);
            } else if (val > minHeap.top()) {
                minHeap.pop();
                minHeap.push(val);
            }
            return minHeap.top();
        }
    };
    
    /*
     * 补充题目6: LeetCode 407. 接雨水 II
     * 链接: https://leetcode.cn/problems/trapping-rain-water-ii/
     * 题目描述: 给定一个 m x n 的矩阵，其中的值都是非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。
     * 
     * 解题思路:
     * 使用最小堆实现的Dijkstra算法变种：
     * 1. 从边界开始，将所有边界点加入最小堆
     * 2. 维护一个visited数组标记已访问的点
     * 3. 每次从堆中取出高度最小的点，向四个方向扩展
     * 4. 如果相邻点未访问过，计算能积累的水量并更新
     * 
     * 时间复杂度: O(m*n log(m+n)) - m,n为矩阵维度，堆操作复杂度O(log(m+n))
     * 空间复杂度: O(m*n) - 存储visited数组
     * 
     * 是否最优解: 是，这是解决二维接雨水问题的最优解法之一
     * 
     * 相关题目:
     * - LeetCode 42. 接雨水
     * - LintCode 364. Trapping Rain Water II
     */
    static int trapRainWater(vector<vector<int>>& heightMap) {
        if (heightMap.empty() || heightMap.size() <= 2 || heightMap[0].size() <= 2) {
            return 0;
        }
        
        int m = heightMap.size();
        int n = heightMap[0].size();
        vector<vector<bool>> visited(m, vector<bool>(n, false));
        
        // 定义优先队列中的元素结构
        struct Cell {
            int row, col, height;
            Cell(int r, int c, int h) : row(r), col(c), height(h) {}
            bool operator>(const Cell& other) const {
                return height > other.height;
            }
        };
        
        // 最小堆，按高度排序
        priority_queue<Cell, vector<Cell>, greater<Cell>> minHeap;
        
        // 初始化：将所有边界点加入堆中
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 || i == m - 1 || j == 0 || j == n - 1) {
                    minHeap.push(Cell(i, j, heightMap[i][j]));
                    visited[i][j] = true;
                }
            }
        }
        
        int water = 0;
        vector<vector<int>> dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上下左右四个方向
        
        // 从边界开始向内部处理
        while (!minHeap.empty()) {
            Cell cell = minHeap.top();
            minHeap.pop();
            
            for (auto& dir : dirs) {
                int newRow = cell.row + dir[0];
                int newCol = cell.col + dir[1];
                
                if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n && !visited[newRow][newCol]) {
                    // 计算当前位置能积累的水量
                    if (heightMap[newRow][newCol] < cell.height) {
                        water += cell.height - heightMap[newRow][newCol];
                    }
                    
                    // 将新点加入堆中，高度取最大值（当前点高度或原始高度）
                    minHeap.push(Cell(newRow, newCol, max(heightMap[newRow][newCol], cell.height)));
                    visited[newRow][newCol] = true;
                }
            }
        }
        
        return water;
    }
    
    /*
     * 补充题目7: LeetCode 264. 丑数 II
     * 链接: https://leetcode.cn/problems/ugly-number-ii/
     * 题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。丑数就是质因子只包含 2、3 和 5 的正整数。
     * 
     * 解题思路:
     * 使用最小堆生成有序的丑数序列：
     * 1. 初始化堆，放入第一个丑数1
     * 2. 使用哈希集合去重
     * 3. 每次从堆中取出最小的丑数，乘以2、3、5生成新的丑数
     * 4. 第n次取出的数即为第n个丑数
     * 
     * 时间复杂度: O(n log n) - 进行n次堆操作，每次O(log n)
     * 空间复杂度: O(n) - 堆和集合的大小
     * 
     * 是否最优解: 不是，更优的解法是使用动态规划，时间复杂度O(n)，空间复杂度O(n)
     * 
     * 相关题目:
     * - LeetCode 313. 超级丑数
     * - 牛客网 丑数系列
     */
    static int nthUglyNumber(int n) {
        if (n <= 0) {
            throw invalid_argument("n must be positive");
        }
        
        // 使用最小堆生成有序丑数
        priority_queue<long, vector<long>, greater<long>> minHeap;
        unordered_set<long> seen;
        
        // 初始丑数为1
        minHeap.push(1L);
        seen.insert(1L);
        
        long ugly = 1;
        // 生成因子
        int factors[] = {2, 3, 5};
        
        // 循环n次，第n次取出的就是第n个丑数
        for (int i = 0; i < n; i++) {
            ugly = minHeap.top();
            minHeap.pop();
            
            // 生成新的丑数
            for (int factor : factors) {
                long next = ugly * factor;
                if (seen.find(next) == seen.end()) {
                    seen.insert(next);
                    minHeap.push(next);
                }
            }
        }
        
        return (int)ugly;
    }
    
    /*
     * 补充题目8: LeetCode 378. 有序矩阵中第 K 小的元素
     * 链接: https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
     * 题目描述: 给你一个 n x n 矩阵 matrix ，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。
     * 
     * 解题思路:
     * 使用最小堆进行多路归并：
     * 1. 初始时将第一列的所有元素加入堆中
     * 2. 每次从堆中取出最小的元素，这是当前的第m小元素
     * 3. 如果m等于k，返回该元素
     * 4. 否则，将该元素所在行的下一个元素加入堆中
     * 
     * 时间复杂度: O(k log n) - k次堆操作，每次O(log n)
     * 空间复杂度: O(n) - 堆的大小最多为n
     * 
     * 是否最优解: 不是，更优的解法是二分查找，时间复杂度O(n log(max-min))
     * 
     * 相关题目:
     * - LeetCode 373. 查找和最小的K对数字
     * - LeetCode 719. 找出第k小的距离对
     */
    static int kthSmallest(vector<vector<int>>& matrix, int k) {
        if (matrix.empty() || matrix[0].empty()) {
            throw invalid_argument("Invalid matrix");
        }
        
        int n = matrix.size();
        
        // 定义矩阵元素结构
        struct MatrixCell {
            int row, col, value;
            MatrixCell(int r, int c, int v) : row(r), col(c), value(v) {}
            bool operator>(const MatrixCell& other) const {
                return value > other.value;
            }
        };
        
        priority_queue<MatrixCell, vector<MatrixCell>, greater<MatrixCell>> minHeap;
        
        // 将第一列的所有元素加入堆中
        for (int i = 0; i < n; i++) {
            minHeap.push(MatrixCell(i, 0, matrix[i][0]));
        }
        
        // 取出k-1个元素，第k次取出的就是第k小的元素
        MatrixCell current(0, 0, 0);
        for (int i = 0; i < k; i++) {
            current = minHeap.top();
            minHeap.pop();
            // 如果当前行还有下一个元素，加入堆中
            if (current.col < n - 1) {
                minHeap.push(MatrixCell(current.row, current.col + 1, matrix[current.row][current.col + 1]));
            }
        }
        
        return current.value;
    }
    
    /*
     * 补充题目9: LeetCode 239. 滑动窗口最大值
     * 链接: https://leetcode.cn/problems/sliding-window-maximum/
     * 题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
     * 
     * 解题思路:
     * 使用最大堆维护滑动窗口内的元素：
     * 1. 维护一个最大堆，存储元素值和索引
     * 2. 窗口滑动时，将新元素加入堆中
     * 3. 检查堆顶元素是否在当前窗口内，如果不在则移除
     * 4. 堆顶元素即为当前窗口的最大值
     * 
     * 时间复杂度: O(n log k) - n个元素，每个元素最多进出堆一次
     * 空间复杂度: O(k) - 堆的大小最多为k
     * 
     * 是否最优解: 不是，更优的解法是使用单调队列，时间复杂度O(n)
     * 
     * 相关题目:
     * - 牛客网 BM45 滑动窗口的最大值
     * - HackerRank Sliding Window Maximum
     */
    static vector<int> maxSlidingWindow(vector<int>& nums, int k) {
        if (nums.empty() || k <= 0) {
            return {};
        }
        
        int n = nums.size();
        vector<int> result(n - k + 1);
        
        // 定义堆元素结构
        struct MaxHeapElement {
            int value, index;
            MaxHeapElement(int v, int i) : value(v), index(i) {}
            bool operator<(const MaxHeapElement& other) const {
                if (value != other.value) return value < other.value;
                return index < other.index;
            }
        };
        
        // 最大堆，按值降序排序，如果值相同，按索引降序排序
        priority_queue<MaxHeapElement> maxHeap;
        
        // 初始化第一个窗口
        for (int i = 0; i < k; i++) {
            maxHeap.push(MaxHeapElement(nums[i], i));
        }
        
        result[0] = maxHeap.top().value;
        
        // 滑动窗口
        for (int i = k; i < n; i++) {
            // 将新元素加入堆
            maxHeap.push(MaxHeapElement(nums[i], i));
            
            // 移除不在当前窗口内的堆顶元素
            while (maxHeap.top().index <= i - k) {
                maxHeap.pop();
            }
            
            // 记录当前窗口的最大值
            result[i - k + 1] = maxHeap.top().value;
        }
        
        return result;
    }
    
    /*
     * 补充题目10: LeetCode 502. IPO
     * 链接: https://leetcode.cn/problems/ipo/
     * 题目描述: 假设 力扣（LeetCode）即将开始 IPO 。为了以更高的价格将股票卖给风险投资公司，力扣 希望在 IPO 之前开展一些项目以增加其资本。由于资源有限，它只能在 IPO 之前完成最多 k 个不同的项目。帮助力扣 设计完成最多 k 个不同项目后得到最大总资本的方式。
     * 
     * 解题思路:
     * 使用两个堆组合解决：
     * 1. 最小堆按资本排序，存储可投资项目
     * 2. 最大堆按利润排序，存储当前可以投资的项目
     * 3. 每次从最小堆中取出所有可以投资的项目（资本<=当前总资本）放入最大堆
     * 4. 从最大堆中取出利润最大的项目投资，增加总资本
     * 5. 重复3-4步骤k次
     * 
     * 时间复杂度: O(N log N) - N为项目数量，排序和堆操作
     * 空间复杂度: O(N) - 堆的大小
     * 
     * 是否最优解: 是，这是解决此类资源分配问题的最优解法
     * 
     * 相关题目:
     * - LeetCode 857. 雇佣 K 名工人的最低成本
     * - LeetCode 1383. 最大的团队表现值
     */
    static int findMaximizedCapital(int k, int w, vector<int>& profits, vector<int>& capital) {
        int n = profits.size();
        
        // 构建项目列表
        vector<pair<int, int>> projects;
        for (int i = 0; i < n; i++) {
            projects.emplace_back(capital[i], profits[i]);
        }
        
        // 按资本升序排序
        sort(projects.begin(), projects.end());
        
        // 最大堆存储利润
        priority_queue<int> maxProfitHeap;
        
        int currentCapital = w;
        int projectIndex = 0;
        
        for (int i = 0; i < k; i++) {
            // 将所有满足资本要求的项目加入最大堆
            while (projectIndex < n && projects[projectIndex].first <= currentCapital) {
                maxProfitHeap.push(projects[projectIndex].second);
                projectIndex++;
            }
            
            // 如果没有可投资的项目，退出循环
            if (maxProfitHeap.empty()) {
                break;
            }
            
            // 选择利润最大的项目投资
            currentCapital += maxProfitHeap.top();
            maxProfitHeap.pop();
        }
        
        return currentCapital;
    }
    
    /*
     * 补充题目11: LeetCode 692. 前K个高频单词
     * 链接: https://leetcode.cn/problems/top-k-frequent-words/
     * 题目描述: 给定一个单词列表 words 和一个整数 k ，返回前 k 个出现次数最多的单词。
     * 
     * 解题思路:
     * 1. 使用哈希表统计每个单词的频率
     * 2. 使用最小堆维护前k个高频单词
     * 3. 自定义比较器：先按频率升序，频率相同按字典序降序
     * 4. 最后反转结果列表
     * 
     * 时间复杂度: O(n log k) - n为单词数量
     * 空间复杂度: O(n) - 哈希表和堆
     * 
     * 是否最优解: 是，满足题目要求的复杂度
     * 
     * 相关题目:
     * - LeetCode 347. 前 K 个高频元素
     * - LintCode 471. 前K个高频单词
     */
    static vector<string> topKFrequentWords(vector<string>& words, int k) {
        // 1. 统计频率
        unordered_map<string, int> freqMap;
        for (const string& word : words) {
            freqMap[word]++;
        }
        
        // 2. 使用最小堆维护前k个高频单词
        // 自定义比较器：频率升序，频率相同按字典序降序
        auto cmp = [&freqMap](const string& a, const string& b) {
            if (freqMap[a] != freqMap[b]) {
                return freqMap[a] > freqMap[b]; // 频率升序
            }
            return a < b; // 字典序降序
        };
        priority_queue<string, vector<string>, decltype(cmp)> minHeap(cmp);
        
        // 3. 遍历频率表，维护堆大小为k
        for (auto& pair : freqMap) {
            if (minHeap.size() < k) {
                minHeap.push(pair.first);
            } else {
                int currentFreq = freqMap[pair.first];
                int minFreq = freqMap[minHeap.top()];
                if (currentFreq > minFreq || 
                    (currentFreq == minFreq && pair.first < minHeap.top())) {
                    minHeap.pop();
                    minHeap.push(pair.first);
                }
            }
        }
        
        // 4. 构造结果列表（需要反转）
        vector<string> result;
        while (!minHeap.empty()) {
            result.push_back(minHeap.top());
            minHeap.pop();
        }
        reverse(result.begin(), result.end());
        
        return result;
    }
    
    /*
     * 堆和堆排序知识点总结：
     * 
     * 1. 堆的定义：
     *    - 堆是一种特殊的完全二叉树
     *    - 大顶堆：父节点的值总是大于或等于其子节点的值
     *    - 小顶堆：父节点的值总是小于或等于其子节点的值
     * 
     * 2. 堆的存储：
     *    - 通常使用数组来存储堆
     *    - 对于索引为i的节点：
     *      - 父节点索引：(i-1)/2
     *      - 左子节点索引：2*i+1
     *      - 右子节点索引：2*i+2
     * 
     * 3. 堆的基本操作：
     *    - heapInsert(i)：向上调整，时间复杂度O(log n)
     *    - heapify(i, size)：向下调整，时间复杂度O(log n)
     *    - 建堆：
     *      - 从顶到底：O(n log n)
     *      - 从底到顶：O(n)
     * 
     * 4. 堆排序：
     *    - 时间复杂度：O(n log n)
     *    - 空间复杂度：O(1)
     *    - 不稳定排序
     * 
     * 5. 堆的应用场景：
     *    - 优先队列实现
     *    - Top K问题（最大/最小的K个元素）
     *    - 数据流中的中位数
     *    - 合并K个有序序列
     *    - Dijkstra算法等图算法
     *    - 资源分配问题（如IPO问题）
     *    - 贪心算法的实现
     *    - 滑动窗口最大值/最小值
     *    - 二维最优化问题（如接雨水II）
     * 
     * 6. C++中的堆实现：
     *    - priority_queue容器适配器
     *    - 默认是最大堆（priority_queue<int>）
     *    - 可以通过greater<T>实现最小堆（priority_queue<int, vector<int>, greater<int>>）
     *    - 支持自定义比较函数或比较类
     *    - 主要操作：push(), pop(), top(), size(), empty()
     *    - 不支持直接访问中间元素
     *    - 不支持在任意位置删除元素
     * 
     * 7. 堆与其他数据结构的比较：
     *    - 与BST比较：堆的构建更快，但BST支持范围查询
     *    - 与普通数组比较：堆支持高效的插入和删除最值操作
     *    - 与平衡树比较：实现更简单，但不支持复杂查询
     *    - 与单调队列比较：对于滑动窗口问题，单调队列效率更高
     * 
     * 8. 工程化考量：
     *    - 异常处理：处理空堆、非法输入、整数溢出等边界情况
     *    - 性能优化：选择合适的堆大小，避免频繁扩容
     *    - 内存管理：在C++中注意资源的释放，避免内存泄漏
     *    - 线程安全：在多线程环境中需要加锁或使用线程安全的实现
     *    - 数据类型选择：对于可能溢出的数据，使用更大的数据类型
     *    - 哈希去重：在生成序列问题中使用哈希集合避免重复
     * 
     * 9. 常见堆相关问题的解题思路：
     *    - Top K问题：使用大小为K的最小堆（最大的K个元素）或最大堆（最小的K个元素）
     *    - 中位数问题：使用两个堆，一个最大堆存储小半部分，一个最小堆存储大半部分
     *    - 合并K个有序序列：使用大小为K的最小堆维护每个序列的当前元素
     *    - 资源分配问题：结合多个堆，分别按不同维度排序
     *    - 二维优化问题：从边界开始，使用最小堆动态扩展
     *    - 滑动窗口问题：维护带索引的堆，过滤过期元素
     * 
     * 10. 堆的优化技巧：
     *    - 使用数组实现堆时，可以预先分配足够的空间减少扩容开销
     *    - 在不需要稳定排序的场景下，堆排序比归并排序更节省空间
     *    - 对于大数据量，可以使用外部堆排序
     *    - 在C++中，可以使用emplace()而不是push()来避免不必要的拷贝
     *    - 对于自定义比较，可以使用lambda表达式简化代码
     * 
     * 11. 与其他技术领域的联系：
     *    - 机器学习：堆用于构建决策树中的最佳分割点选择
     *    - 大数据处理：堆在MapReduce等框架中用于排序和聚合
     *    - 操作系统：进程调度算法中使用优先队列管理任务优先级
     *    - 图算法：最短路径算法（Dijkstra）、最小生成树（Prim）等
     *    - 自然语言处理：文本排序、词频统计等任务
     * 
     * 12. 更多堆相关题目列表（来自各大算法平台）：
     *    
     *    LeetCode题目：
     *    - #215: Kth Largest Element in an Array (数组中的第K个最大元素)
     *    - #23: Merge k Sorted Lists (合并K个排序链表)
     *    - #295: Find Median from Data Stream (数据流的中位数)
     *    - #347: Top K Frequent Elements (前K个高频元素)
     *    - #703: Kth Largest Element in a Stream (数据流的第K大元素)
     *    - #407: Trapping Rain Water II (接雨水II)
     *    - #264: Ugly Number II (丑数II)
     *    - #378: Kth Smallest Element in a Sorted Matrix (有序矩阵中第K小的元素)
     *    - #239: Sliding Window Maximum (滑动窗口最大值)
     *    - #502: IPO (IPO)
     *    - #692: Top K Frequent Words (前K个高频单词)
     *    - #451: Sort Characters By Frequency (根据字符出现频率排序)
     *    - #373: Find K Pairs with Smallest Sums (查找和最小的K对数字)
     *    - #253: Meeting Rooms II (会议室II)
     *    - #218: The Skyline Problem (天际线问题)
     *    - #778: Swim in Rising Water (水位上升的泳池中游泳)
     *    - #355: Design Twitter (设计推特)
     *    - #313: Super Ugly Number (超级丑数)
     *    - #719: Find K-th Smallest Pair Distance (找出第k小的距离对)
     *    - #659: Split Array into Consecutive Subsequences (分割数组为连续子序列)
     *    
     *    LintCode题目：
     *    - #130: Heapify (建堆)
     *    - #104: Merge K Sorted Lists (合并K个排序链表)
     *    - #612: K Closest Points (最近K个点)
     *    - #545: Top k Largest Numbers II (前K个最大数II)
     *    - #461: Kth Smallest Numbers in Unsorted Array (无序数组中的第K小元素)
     *    - #364: Trapping Rain Water II (接雨水II)
     *    - #460: Find K Closest Elements (寻找K个最接近的元素)
     *    
     *    HackerRank题目：
     *    - QHEAP1 (基本堆操作)
     *    - Find the Running Median (寻找运行中位数)
     *    - Jesse and Cookies (杰茜和饼干)
     *    - Minimum Average Waiting Time (最小平均等待时间)
     *    - Running Median (运行中位数)
     *    - K Largest Elements (K个最大元素)
     *    
     *    洛谷题目：
     *    - P1177 【模板】排序
     *    - P1090 合并果子 (贪心+堆)
     *    - P3378 【模板】堆
     *    - P1631 序列合并
     *    - P2085 最小函数值
     *    
     *    牛客题目：
     *    - BM45 滑动窗口的最大值
     *    - BM46 最小的K个数
     *    - BM47 寻找第K大的数
     *    - JZ40 最小的K个数
     *    - JZ41 数据流中的中位数
     *    
     *    Codeforces题目：
     *    - A. Helpful Maths
     *    - B. Sort the Array
     *    - C. Maximum Subsequence Value
     *    - D. Queue
     *    - E. Heap Operations
     *    
     *    AtCoder题目：
     *    - ABC 127F (对顶堆动态维护中位数)
     *    - ABC 141D (优先队列贪心)
     *    - ABC 151E (优先队列应用)
     *    - ABC 161D (优先队列优化)
     *    
     *    剑指Offer题目：
     *    - 剑指Offer 40. 最小的k个数
     *    - 剑指Offer 41. 数据流中的中位数
     *    - 剑指Offer 59-II. 队列的最大值
     *    - 剑指Offer II 059. 数据流的第K大数值
     *    - 剑指Offer II 060. 出现频率最高的k个数字
     */
};

// 添加main函数以便编译和测试
int main() {
    // 简单测试代码
    vector<int> test = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
    cout << "Original array: ";
    for (int num : test) {
        cout << num << " ";
    }
    cout << endl;
    
    HeapSortSolution::sortArray(test);
    
    cout << "Sorted array: ";
    for (int num : test) {
        cout << num << " ";
    }
    cout << endl;
    
    return 0;
}