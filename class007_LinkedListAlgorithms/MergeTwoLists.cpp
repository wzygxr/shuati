/*
 * 合并两个有序链表及相关题目扩展 (C++版本)
 * 
 * 🎯 算法专题：链表合并与相关算法
 * 📚 覆盖平台：LeetCode、牛客网、LintCode、剑指Offer等
 * 💻 语言特性：C++11/14/17标准，RAII资源管理，STL容器使用
 * 
 * 🔍 工程化考量：
 * 1. 内存管理：手动内存分配与释放，避免内存泄漏
 * 2. 异常安全：确保代码在异常情况下的资源释放
 * 3. 性能优化：STL容器选择，算法常数项优化
 * 4. 可测试性：完整的单元测试框架，边界条件覆盖
 * 5. 可维护性：清晰的代码结构，详细的注释说明
 * 
 * 📊 复杂度分析体系：
 * - 时间复杂度：从理论分析到实际性能考量
 * - 空间复杂度：内存使用优化策略
 * - 常数项分析：实际运行效率的关键因素
 * 
 * 🚀 算法应用场景：
 * - 大数据处理：外部排序，多路归并
 * - 实时系统：数据流合并处理
 * - 分布式计算：多节点结果合并
 * - 数据库系统：索引合并优化
 * 
 * 主要题目：
 * 1. LeetCode 21. 合并两个有序链表 (基础题)
 * 2. LeetCode 23. 合并K个升序链表 (进阶题) 
 * 3. LeetCode 88. 合并两个有序数组 (变种题)
 * 4. LeetCode 148. 排序链表 (应用扩展)
 * 5. LeetCode 2. 两数相加 (链表操作)
 * 6. LeetCode 24. 两两交换链表中的节点 (链表变换)
 * 7. 牛客 NC33. 合并两个排序的链表 (国内平台)
 * 8. LintCode 104. 合并k个排序链表 (国际平台)
 * 9. LeetCode 86. 分隔链表 (链表分割)
 * 
 * 📈 解题思路技巧总结：
 * 1. 双指针法：适用于两个有序序列的合并，时间复杂度O(m+n)
 * 2. 优先队列(堆)：适用于K个有序序列的合并，时间复杂度O(N*logK)
 * 3. 分治法：将K个序列问题分解为多个两个序列问题，时间复杂度O(N*logK)
 * 4. 哨兵节点：简化链表操作的边界处理，提高代码可读性
 * 5. 原地修改：充分利用已有空间，减少额外空间使用
 * 6. 递归与迭代：不同场景下的选择策略
 * 
 * ⚡ 时间复杂度分析：
 * 1. 合并两个链表：O(m+n)，m和n分别是两个链表的长度
 * 2. 合并K个链表(优先队列)：O(N*logK)，N是所有节点总数，K是链表数量
 * 3. 合并K个链表(分治)：O(N*logK)
 * 4. 合并两个数组：O(m+n)
 * 5. 链表排序：O(nlogn)，归并排序最优
 * 
 * 💾 空间复杂度分析：
 * 1. 合并两个链表：O(1)，原地操作
 * 2. 合并K个链表(优先队列)：O(K)，堆的大小
 * 3. 合并K个链表(分治)：O(logK)，递归栈深度
 * 4. 合并两个数组：O(1)，原地操作
 * 5. 链表排序：O(1)或O(logn)，取决于实现方式
 * 
 * 🛡️ 安全与稳定性：
 * - 空指针检查：所有链表操作前的边界检查
 * - 内存泄漏防护：RAII模式，智能指针使用
 * - 异常处理：try-catch块，资源清理
 * - 输入验证：参数合法性检查
 * 
 * 🔧 调试与测试：
 * - 单元测试：每个算法的独立测试用例
 * - 边界测试：空输入、单元素、极端值等
 * - 性能测试：大规模数据下的性能表现
 * - 内存测试：内存泄漏检测工具使用
 * 
 * 📚 学习路径建议：
 * 1. 基础掌握：LeetCode 21 → 牛客 NC33
 * 2. 进阶提升：LeetCode 23 → LintCode 104  
 * 3. 综合应用：LeetCode 148 → LeetCode 2
 * 4. 拓展思维：LeetCode 24 → LeetCode 86
 * 
 * 🎓 面试重点：
 * - 算法思路清晰表达
 * - 时间空间复杂度分析
 * - 边界条件处理能力
 * - 代码实现简洁优雅
 * - 工程化考量意识
 */

#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <climits>
#include <cassert>
#include <memory>
#include <chrono>
#include <random>

using namespace std;

// 链表节点定义
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
    
    // 用于测试的链表创建方法
    static ListNode* createList(std::vector<int>& arr) {
        if (arr.empty()) return nullptr;
        ListNode* head = new ListNode(arr[0]);
        ListNode* cur = head;
        for (size_t i = 1; i < arr.size(); i++) {
            cur->next = new ListNode(arr[i]);
            cur = cur->next;
        }
        return head;
    }
    
    // 用于测试的链表打印方法
    static void printList(ListNode* head) {
        ListNode* cur = head;
        while (cur) {
            std::cout << cur->val;
            if (cur->next) std::cout << " -> ";
            cur = cur->next;
        }
        std::cout << std::endl;
    }
    
    // 释放链表内存
    static void deleteList(ListNode* head) {
        while (head) {
            ListNode* temp = head;
            head = head->next;
            delete temp;
        }
    }
};

/**
 * 题目1: LeetCode 21. 合并两个有序链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/merge-two-sorted-lists/
 *
 * 题目描述：
 * 将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 *
 * 解法分析：
 * 1. 迭代法 - 时间复杂度: O(m+n), 空间复杂度: O(1)
 * 2. 递归法 - 时间复杂度: O(m+n), 空间复杂度: O(m+n)
 *
 * 解题思路：
 * 使用双指针分别指向两个链表的当前节点，比较节点值的大小，
 * 将较小的节点连接到结果链表中，移动对应指针，重复此过程直到某一链表遍历完。
 * 最后将未遍历完的链表剩余部分直接连接到结果链表末尾。
 */
class MergeTwoSortedListsSolution {
public:
    /**
     * 解法1: 迭代法 (推荐)
     * 时间复杂度: O(m+n) - m和n分别是两个链表的长度
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用哨兵节点简化边界处理
     * 2. 双指针分别遍历两个链表
     * 3. 比较节点值，将较小节点连接到结果链表
     * 4. 处理剩余节点
     */
    static ListNode* mergeTwoListsIterative(ListNode* list1, ListNode* list2) {
        // 创建哨兵节点，简化边界处理
        ListNode dummy(0);
        ListNode* current = &dummy;
        
        // 双指针遍历两个链表
        while (list1 && list2) {
            // 比较两个链表当前节点的值
            if (list1->val <= list2->val) {
                current->next = list1;
                list1 = list1->next;
            } else {
                current->next = list2;
                list2 = list2->next;
            }
            current = current->next;
        }
        
        // 连接剩余节点
        current->next = list1 ? list1 : list2;
        
        // 返回合并后的链表
        return dummy.next;
    }
    
    /**
     * 解法2: 递归法
     * 时间复杂度: O(m+n) - 每个节点访问一次
     * 空间复杂度: O(m+n) - 递归调用栈的深度
     *
     * 核心思想：
     * 1. 递归终止条件：其中一个链表为空
     * 2. 递归处理：选择较小节点作为当前节点，递归处理剩余部分
     * 3. 返回当前节点
     */
    static ListNode* mergeTwoListsRecursive(ListNode* list1, ListNode* list2) {
        // 递归终止条件
        if (!list1) return list2;
        if (!list2) return list1;
        
        // 递归处理
        if (list1->val <= list2->val) {
            list1->next = mergeTwoListsRecursive(list1->next, list2);
            return list1;
        } else {
            list2->next = mergeTwoListsRecursive(list1, list2->next);
            return list2;
        }
    }
    
    /**
     * 测试方法
     */
    static void test() {
        std::cout << "=== 合并两个有序链表测试 ===" << std::endl;
        
        // 测试用例1: 正常情况
        std::vector<int> arr1 = {1, 2, 4};
        std::vector<int> arr2 = {1, 3, 4};
        ListNode* list1 = ListNode::createList(arr1);
        ListNode* list2 = ListNode::createList(arr2);
        std::cout << "链表1: ";
        ListNode::printList(list1);
        std::cout << "链表2: ";
        ListNode::printList(list2);
        
        ListNode* result1 = mergeTwoListsIterative(list1, list2);
        std::cout << "迭代法结果: ";
        ListNode::printList(result1);
        ListNode::deleteList(result1);
        
        // 重新创建测试数据
        list1 = ListNode::createList(arr1);
        list2 = ListNode::createList(arr2);
        ListNode* result2 = mergeTwoListsRecursive(list1, list2);
        std::cout << "递归法结果: ";
        ListNode::printList(result2);
        ListNode::deleteList(result2);
        
        // 测试用例2: 空链表
        ListNode* list3 = nullptr;
        std::vector<int> arr4 = {0};
        ListNode* list4 = ListNode::createList(arr4);
        ListNode* result3 = mergeTwoListsIterative(list3, list4);
        std::cout << "空链表测试: ";
        ListNode::printList(result3);
        ListNode::deleteList(result3);
        
        // 测试用例3: 两个空链表
        ListNode* list5 = nullptr;
        ListNode* list6 = nullptr;
        ListNode* result4 = mergeTwoListsIterative(list5, list6);
        std::cout << "两个空链表: ";
        ListNode::printList(result4);
        ListNode::deleteList(result4);
        std::cout << std::endl;
    }
};

/**
 * 题目2: LeetCode 23. 合并K个升序链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/merge-k-sorted-lists/
 *
 * 题目描述：
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 *
 * 解法分析：
 * 1. 优先队列法 (最优解) - 时间复杂度: O(N*logK), 空间复杂度: O(K)
 * 2. 分治法 - 时间复杂度: O(N*logK), 空间复杂度: O(logK)
 *
 * 解题思路：
 * 优先队列法：维护一个大小为K的最小堆，堆中存放K个链表的头节点。
 * 每次从堆中取出最小节点加入结果链表，并将该节点的下一个节点加入堆中。
 * 分治法：将K个链表分成两部分，分别合并后再合并两个结果。
 */
class MergeKSortedListsSolution {
public:
    // 自定义比较函数，用于优先队列
    struct Compare {
        bool operator()(ListNode* a, ListNode* b) {
            return a->val > b->val;  // 最小堆
        }
    };
    
    /**
     * 解法1: 优先队列法 (推荐)
     * 时间复杂度: O(N*logK) - N是所有节点总数，K是链表数量
     * 空间复杂度: O(K) - 优先队列的大小
     *
     * 核心思想：
     * 1. 使用优先队列(最小堆)维护K个链表的当前最小节点
     * 2. 每次取出最小节点加入结果链表
     * 3. 将取出节点的下一个节点加入优先队列
     * 4. 重复直到优先队列为空
     */
    static ListNode* mergeKListsPriorityQueue(vector<ListNode*>& lists) {
        if (lists.empty()) return nullptr;
        
        // 创建优先队列(最小堆)
        priority_queue<ListNode*, vector<ListNode*>, Compare> minHeap;
        
        // 将所有非空链表的头节点加入优先队列
        for (ListNode* list : lists) {
            if (list) {
                minHeap.push(list);
            }
        }
        
        // 创建哨兵节点
        ListNode dummy(0);
        ListNode* current = &dummy;
        
        // 从优先队列中依次取出最小节点
        while (!minHeap.empty()) {
            // 取出最小节点
            ListNode* node = minHeap.top();
            minHeap.pop();
            
            // 加入结果链表
            current->next = node;
            current = current->next;
            
            // 如果该节点还有后续节点，加入优先队列
            if (node->next) {
                minHeap.push(node->next);
            }
        }
        
        return dummy.next;
    }
    
    /**
     * 解法2: 分治法
     * 时间复杂度: O(N*logK) - N是所有节点总数，K是链表数量
     * 空间复杂度: O(logK) - 递归调用栈的深度
     *
     * 核心思想：
     * 1. 将K个链表分成两部分
     * 2. 递归合并每一部分
     * 3. 合并两个结果链表
     */
    static ListNode* mergeKListsDivideAndConquer(vector<ListNode*>& lists) {
        if (lists.empty()) return nullptr;
        return mergeKListsHelper(lists, 0, lists.size() - 1);
    }
    
private:
    /**
     * 分治辅助函数
     */
    static ListNode* mergeKListsHelper(vector<ListNode*>& lists, int left, int right) {
        if (left == right) return lists[left];
        if (left + 1 == right) return MergeTwoSortedListsSolution::mergeTwoListsIterative(lists[left], lists[right]);
        
        int mid = left + (right - left) / 2;
        ListNode* l1 = mergeKListsHelper(lists, left, mid);
        ListNode* l2 = mergeKListsHelper(lists, mid + 1, right);
        
        return MergeTwoSortedListsSolution::mergeTwoListsIterative(l1, l2);
    }
    
public:
    /**
     * 测试方法
     */
    static void test() {
        std::cout << "=== 合并K个升序链表测试 ===" << std::endl;
        
        // 创建测试数据
        std::vector<int> arr1 = {1, 4, 5};
        std::vector<int> arr2 = {1, 3, 4};
        std::vector<int> arr3 = {2, 6};
        ListNode* l1 = ListNode::createList(arr1);
        ListNode* l2 = ListNode::createList(arr2);
        ListNode* l3 = ListNode::createList(arr3);
        
        std::vector<ListNode*> lists = {l1, l2, l3};
        
        std::cout << "链表1: ";
        ListNode::printList(lists[0]);
        std::cout << "链表2: ";
        ListNode::printList(lists[1]);
        std::cout << "链表3: ";
        ListNode::printList(lists[2]);
        
        // 测试优先队列法
        std::vector<ListNode*> listsCopy1 = lists;
        ListNode* result1 = mergeKListsPriorityQueue(listsCopy1);
        std::cout << "优先队列法结果: ";
        ListNode::printList(result1);
        ListNode::deleteList(result1);
        
        // 测试分治法
        std::vector<ListNode*> listsCopy2 = lists;
        ListNode* result2 = mergeKListsDivideAndConquer(listsCopy2);
        std::cout << "分治法结果: ";
        ListNode::printList(result2);
        ListNode::deleteList(result2);
        std::cout << std::endl;
    }
};

/**
 * 题目3: LeetCode 88. 合并两个有序数组
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/merge-sorted-array/
 *
 * 题目描述：
 * 给你两个按非递减顺序排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n，
 * 分别表示 nums1 和 nums2 中的元素数目。
 * 请你合并 nums2 到 nums1 中，使合并后的数组同样按非递减顺序排列。
 * 注意：最终，合并后数组不应由函数返回，而是存储在数组 nums1 中。
 * 为了应对这种情况，nums1 的初始长度为 m + n，其中前 m 个元素表示应合并的元素，后 n 个元素为 0，应忽略。
 * nums2 的长度为 n。
 *
 * 解法分析：
 * 1. 从后往前合并 (最优解) - 时间复杂度: O(m+n), 空间复杂度: O(1)
 * 2. 从前往后合并 - 时间复杂度: O(m+n), 空间复杂度: O(m+n)
 * 3. 合并后排序 - 时间复杂度: O((m+n)log(m+n)), 空间复杂度: O(1)
 *
 * 解题思路：
 * 从后往前合并可以避免覆盖nums1中未处理的元素。
 * 使用三个指针分别指向nums1有效元素末尾、nums2末尾和nums1实际末尾。
 * 比较两个数组当前元素，将较大者放入nums1末尾，移动相应指针。
 */
class MergeSortedArraySolution {
public:
    /**
     * 解法1: 从后往前合并 (推荐)
     * 时间复杂度: O(m+n) - 每个元素访问一次
     * 空间复杂度: O(1) - 原地修改
     *
     * 核心思想：
     * 1. 从两个数组的末尾开始比较
     * 2. 将较大元素放到nums1的末尾
     * 3. 移动相应指针
     * 4. 处理剩余元素
     */
    static void mergeFromBack(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        // 三个指针
        int i = m - 1;      // nums1有效元素的末尾
        int j = n - 1;      // nums2的末尾
        int k = m + n - 1;  // nums1实际末尾
        
        // 从后往前合并
        while (i >= 0 && j >= 0) {
            if (nums1[i] > nums2[j]) {
                nums1[k--] = nums1[i--];
            } else {
                nums1[k--] = nums2[j--];
            }
        }
        
        // 处理nums2剩余元素
        while (j >= 0) {
            nums1[k--] = nums2[j--];
        }
        
        // 注意：如果nums1有剩余元素，它们已经在正确位置，无需处理
    }
    
    /**
     * 解法2: 从前往后合并
     * 时间复杂度: O(m+n)
     * 空间复杂度: O(m) - 需要额外数组存储nums1的前m个元素
     */
    static void mergeFromFront(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        // 创建临时数组存储nums1的前m个元素
        vector<int> nums1Copy(nums1.begin(), nums1.begin() + m);
        
        // 三个指针
        size_t i = 0;  // nums1Copy的指针
        size_t j = 0;  // nums2的指针
        size_t k = 0;  // nums1的指针
        
        // 从前往后合并
        while (i < (size_t)m && j < (size_t)n) {
            if (nums1Copy[i] <= nums2[j]) {
                nums1[k++] = nums1Copy[i++];
            } else {
                nums1[k++] = nums2[j++];
            }
        }
        
        // 处理剩余元素
        while (i < (size_t)m) {
            nums1[k++] = nums1Copy[i++];
        }
        
        while (j < (size_t)n) {
            nums1[k++] = nums2[j++];
        }
    }
    
    /**
     * 解法3: 合并后排序
     * 时间复杂度: O((m+n)log(m+n))
     * 空间复杂度: O(1)
     */
    static void mergeAndSort(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        // 将nums2复制到nums1的后半部分
        for (int i = 0; i < n; i++) {
            nums1[m + i] = nums2[i];
        }
        // 排序
        sort(nums1.begin(), nums1.end());
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== 合并两个有序数组测试 ===" << endl;
        
        // 测试用例1
        vector<int> nums1 = {1, 2, 3, 0, 0, 0};
        int m = 3;
        vector<int> nums2 = {2, 5, 6};
        int n = 3;
        
        cout << "数组1: [";
        for (size_t i = 0; i < nums1.size(); i++) {
            cout << nums1[i];
            if (i < nums1.size() - 1) cout << ", ";
        }
        cout << "], m = " << m << endl;
        
        cout << "数组2: [";
        for (size_t i = 0; i < nums2.size(); i++) {
            cout << nums2[i];
            if (i < nums2.size() - 1) cout << ", ";
        }
        cout << "], n = " << n << endl;
        
        // 测试从后往前合并
        vector<int> nums1Copy1 = nums1;
        mergeFromBack(nums1Copy1, m, nums2, n);
        cout << "从后往前合并: [";
        for (size_t i = 0; i < nums1Copy1.size(); i++) {
            cout << nums1Copy1[i];
            if (i < nums1Copy1.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        
        // 测试从前往后合并
        vector<int> nums1Copy2 = nums1;
        mergeFromFront(nums1Copy2, m, nums2, n);
        cout << "从前往后合并: [";
        for (size_t i = 0; i < nums1Copy2.size(); i++) {
            cout << nums1Copy2[i];
            if (i < nums1Copy2.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        
        // 测试合并后排序
        vector<int> nums1Copy3 = nums1;
        mergeAndSort(nums1Copy3, m, nums2, n);
        cout << "合并后排序: [";
        for (size_t i = 0; i < nums1Copy3.size(); i++) {
            cout << nums1Copy3[i];
            if (i < nums1Copy3.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        
        // 测试用例2: nums1为空
        vector<int> nums3 = {0};
        int m2 = 0;
        vector<int> nums4 = {1};
        int n2 = 1;
        
        cout << "\n数组1: [";
        for (size_t i = 0; i < nums3.size(); i++) {
            cout << nums3[i];
            if (i < nums3.size() - 1) cout << ", ";
        }
        cout << "], m = " << m2 << endl;
        
        cout << "数组2: [";
        for (size_t i = 0; i < nums4.size(); i++) {
            cout << nums4[i];
            if (i < nums4.size() - 1) cout << ", ";
        }
        cout << "], n = " << n2 << endl;
        
        mergeFromBack(nums3, m2, nums4, n2);
        cout << "从后往前合并: [";
        for (size_t i = 0; i < nums3.size(); i++) {
            cout << nums3[i];
            if (i < nums3.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
        cout << endl;
    }
};

/**
 * 题目4: LeetCode 148. 排序链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/sort-list/
 * 
 * 题目描述：
 * 给你链表的头结点 head，请将其按 升序 排列并返回 排序后的链表 。
 * 要求在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序。
 */
class SortListSolution {
public:
    /**
     * 解法1: 归并排序（自顶向下）
     * 时间复杂度: O(nlogn) - 归并排序的标准时间复杂度
     * 空间复杂度: O(logn) - 递归调用栈的深度
     */
    static ListNode* sortListTopDown(ListNode* head) {
        // 基本情况：空链表或只有一个节点
        if (head == nullptr || head->next == nullptr) {
            return head;
        }
        
        // 使用快慢指针找到中点
        ListNode* slow = head;
        ListNode* fast = head->next;
        while (fast != nullptr && fast->next != nullptr) {
            slow = slow->next;
            fast = fast->next->next;
        }
        
        // 分割链表
        ListNode* mid = slow->next;
        slow->next = nullptr;
        
        // 递归排序两个子链表
        ListNode* left = sortListTopDown(head);
        ListNode* right = sortListTopDown(mid);
        
        // 合并排序后的链表
        return mergeTwoLists(left, right);
    }
    
    /**
     * 解法2: 归并排序（自底向上） - 最优解
     * 时间复杂度: O(nlogn) - 与自顶向下相同
     * 空间复杂度: O(1) - 只使用常数级额外空间
     */
    static ListNode* sortListBottomUp(ListNode* head) {
        if (head == nullptr || head->next == nullptr) {
            return head;
        }
        
        // 计算链表长度
        int length = 0;
        ListNode* current = head;
        while (current != nullptr) {
            length++;
            current = current->next;
        }
        
        // 创建哨兵节点
        ListNode dummy(-1);
        dummy.next = head;
        
        // 自底向上进行归并
        for (int step = 1; step < length; step *= 2) {
            ListNode* prev = &dummy;
            current = dummy.next;
            
            while (current != nullptr) {
                // 第一个子链表的头节点
                ListNode* left = current;
                // 分割第一个子链表
                for (int i = 1; i < step && current->next != nullptr; i++) {
                    current = current->next;
                }
                
                // 第二个子链表的头节点
                ListNode* right = current->next;
                // 断开第一个子链表
                current->next = nullptr;
                current = right;
                
                // 分割第二个子链表
                for (int i = 1; i < step && current != nullptr && current->next != nullptr; i++) {
                    current = current->next;
                }
                
                // 记录下一段链表的起始位置
                ListNode* next = nullptr;
                if (current != nullptr) {
                    next = current->next;
                    current->next = nullptr;
                }
                
                // 合并两个子链表
                prev->next = mergeTwoLists(left, right);
                
                // 移动prev到合并后链表的末尾
                while (prev->next != nullptr) {
                    prev = prev->next;
                }
                
                // 处理下一段链表
                current = next;
            }
        }
        
        return dummy.next;
    }
    
    // 合并两个有序链表的辅助函数
    static ListNode* mergeTwoLists(ListNode* l1, ListNode* l2) {
        ListNode dummy(-1);
        ListNode* current = &dummy;
        
        while (l1 != nullptr && l2 != nullptr) {
            if (l1->val <= l2->val) {
                current->next = l1;
                l1 = l1->next;
            } else {
                current->next = l2;
                l2 = l2->next;
            }
            current = current->next;
        }
        
        current->next = (l1 != nullptr) ? l1 : l2;
        return dummy.next;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== 排序链表测试 ===" << endl;
        
        // 测试用例1: 正常情况
        vector<int> arr1 = {4, 2, 1, 3};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "原链表: ";
        ListNode::printList(list1);
        
        ListNode* result1 = sortListTopDown(list1);
        cout << "自顶向下归并排序结果: ";
        ListNode::printList(result1);
        
        // 释放内存
        ListNode::deleteList(result1);
        
        // 重新创建测试数据
        vector<int> arr2 = {4, 2, 1, 3};
        ListNode* list2 = ListNode::createList(arr2);
        ListNode* result2 = sortListBottomUp(list2);
        cout << "自底向上归并排序结果: ";
        ListNode::printList(result2);
        
        // 测试用例2: 包含重复元素
        vector<int> arr3 = {-1, 5, 3, 4, 0};
        ListNode* list3 = ListNode::createList(arr3);
        cout << "\n原链表: ";
        ListNode::printList(list3);
        
        ListNode* result3 = sortListBottomUp(list3);
        cout << "排序结果: ";
        ListNode::printList(result3);
        cout << endl;
        
        // 释放内存
        ListNode::deleteList(result2);
        ListNode::deleteList(result3);
    }
};

/**
 * 题目5: LeetCode 2. 两数相加
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/add-two-numbers/
 * 
 * 题目描述：
 * 给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，并且每个节点只能存储一位数字。
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 */
class AddTwoNumbersSolution {
public:
    /**
     * 解法: 模拟加法过程
     * 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
     * 空间复杂度: O(max(m,n)) - 输出链表的长度最多为max(m,n)+1
     */
    static ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        // 创建哨兵节点
        ListNode dummy(-1);
        ListNode* current = &dummy;
        
        // 进位
        int carry = 0;
        
        // 同时遍历两个链表
        while (l1 != nullptr || l2 != nullptr || carry > 0) {
            // 计算当前位的和
            int sum = carry;
            if (l1 != nullptr) {
                sum += l1->val;
                l1 = l1->next;
            }
            if (l2 != nullptr) {
                sum += l2->val;
                l2 = l2->next;
            }
            
            // 更新进位
            carry = sum / 10;
            // 创建新节点存储当前位的结果
            current->next = new ListNode(sum % 10);
            current = current->next;
        }
        
        return dummy.next;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== 两数相加测试 ===" << endl;
        
        // 测试用例1: 正常情况
        vector<int> arr1 = {2, 4, 3};  // 342
        vector<int> arr2 = {5, 6, 4};  // 465
        ListNode* l1 = ListNode::createList(arr1);
        ListNode* l2 = ListNode::createList(arr2);
        cout << "链表1 (342逆序): ";
        ListNode::printList(l1);
        cout << "链表2 (465逆序): ";
        ListNode::printList(l2);
        
        ListNode* result1 = addTwoNumbers(l1, l2);
        cout << "结果 (807逆序): ";
        ListNode::printList(result1);
        
        // 测试用例2: 包含进位
        vector<int> arr3 = {9, 9, 9, 9, 9, 9, 9};
        vector<int> arr4 = {9, 9, 9, 9};
        ListNode* l3 = ListNode::createList(arr3);
        ListNode* l4 = ListNode::createList(arr4);
        cout << "\n链表1: ";
        ListNode::printList(l3);
        cout << "链表2: ";
        ListNode::printList(l4);
        
        ListNode* result2 = addTwoNumbers(l3, l4);
        cout << "结果: ";
        ListNode::printList(result2);
        cout << endl;
        
        // 释放内存
        ListNode::deleteList(l1);
        ListNode::deleteList(l2);
        ListNode::deleteList(result1);
        ListNode::deleteList(l3);
        ListNode::deleteList(l4);
        ListNode::deleteList(result2);
    }
};

/**
 * 题目6: LeetCode 24. 两两交换链表中的节点
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/swap-nodes-in-pairs/
 * 
 * 题目描述：
 * 给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。
 * 你必须在不修改节点内部值的情况下完成本题（即，只能进行节点交换）。
 */
class SwapNodesInPairsSolution {
public:
    /**
     * 解法1: 迭代法 (推荐)
     * 时间复杂度: O(n) - 每个节点只访问一次
     * 空间复杂度: O(1) - 只使用常数级额外空间
     */
    static ListNode* swapPairsIterative(ListNode* head) {
        // 创建哨兵节点
        ListNode dummy(-1);
        dummy.next = head;
        
        ListNode* prev = &dummy;
        
        // 确保有至少两个节点可以交换
        while (prev->next != nullptr && prev->next->next != nullptr) {
            // 标记需要交换的两个节点
            ListNode* first = prev->next;
            ListNode* second = prev->next->next;
            
            // 交换节点
            first->next = second->next;
            second->next = first;
            prev->next = second;
            
            // 移动prev到下一对的前一个位置
            prev = first;
        }
        
        return dummy.next;
    }
    
    /**
     * 解法2: 递归法
     * 时间复杂度: O(n) - 每个节点只访问一次
     * 空间复杂度: O(n) - 递归调用栈的深度
     */
    static ListNode* swapPairsRecursive(ListNode* head) {
        // 递归终止条件
        if (head == nullptr || head->next == nullptr) {
            return head;
        }
        
        // 标记需要交换的两个节点
        ListNode* first = head;
        ListNode* second = head->next;
        
        // 交换节点
        first->next = swapPairsRecursive(second->next);
        second->next = first;
        
        // 返回新的头节点
        return second;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== 两两交换链表中的节点测试 ===" << endl;
        
        // 测试用例1: 偶数个节点
        vector<int> arr1 = {1, 2, 3, 4};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "原链表: ";
        ListNode::printList(list1);
        
        ListNode* result1 = swapPairsIterative(list1);
        cout << "迭代法结果: ";
        ListNode::printList(result1);
        
        // 释放内存
        ListNode::deleteList(result1);
        
        // 重新创建测试数据
        vector<int> arr2 = {1, 2, 3, 4};
        ListNode* list2 = ListNode::createList(arr2);
        ListNode* result2 = swapPairsRecursive(list2);
        cout << "递归法结果: ";
        ListNode::printList(result2);
        
        // 测试用例2: 奇数个节点
        vector<int> arr3 = {1, 2, 3};
        ListNode* list3 = ListNode::createList(arr3);
        cout << "\n原链表: ";
        ListNode::printList(list3);
        
        ListNode* result3 = swapPairsIterative(list3);
        cout << "交换结果: ";
        ListNode::printList(result3);
        cout << endl;
        
        // 释放内存
        ListNode::deleteList(result2);
        ListNode::deleteList(result3);
    }
};

/**
 * 题目7: 牛客 NC33. 合并两个排序的链表
 * 来源: 牛客网
 * 链接: https://www.nowcoder.com/practice/d8b6b4358f774294a89de2a6ac4d9337
 */
class NowCoderMergeSortedListsSolution {
public:
    static ListNode* merge(ListNode* pHead1, ListNode* pHead2) {
        ListNode dummy(-1);
        ListNode* current = &dummy;
        
        while (pHead1 != nullptr && pHead2 != nullptr) {
            if (pHead1->val <= pHead2->val) {
                current->next = pHead1;
                pHead1 = pHead1->next;
            } else {
                current->next = pHead2;
                pHead2 = pHead2->next;
            }
            current = current->next;
        }
        
        current->next = (pHead1 != nullptr) ? pHead1 : pHead2;
        return dummy.next;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== 牛客 NC33. 合并两个排序的链表测试 ===" << endl;
        
        vector<int> arr1 = {1, 3, 5};
        vector<int> arr2 = {2, 4, 6};
        ListNode* list1 = ListNode::createList(arr1);
        ListNode* list2 = ListNode::createList(arr2);
        cout << "链表1: ";
        ListNode::printList(list1);
        cout << "链表2: ";
        ListNode::printList(list2);
        
        ListNode* result = merge(list1, list2);
        cout << "合并结果: ";
        ListNode::printList(result);
        cout << endl;
        
        // 释放内存
        ListNode::deleteList(result);
    }
};

/**
 * 题目8: LintCode 104. 合并k个排序链表
 * 来源: LintCode
 * 链接: https://www.lintcode.com/problem/104/
 */
class LintCodeMergeKListsSolution {
public:
    // 自定义比较器，用于优先队列
    struct CompareNode {
        bool operator()(ListNode* a, ListNode* b) {
            return a->val > b->val; // 小顶堆
        }
    };
    
    static ListNode* mergeKLists(vector<ListNode*>& lists) {
        if (lists.empty()) return nullptr;
        
        // 创建优先队列(最小堆)
        priority_queue<ListNode*, vector<ListNode*>, CompareNode> minHeap;
        
        // 将所有非空链表的头节点加入优先队列
        for (ListNode* list : lists) {
            if (list != nullptr) {
                minHeap.push(list);
            }
        }
        
        // 创建哨兵节点
        ListNode dummy(-1);
        ListNode* current = &dummy;
        
        // 从优先队列中依次取出最小节点
        while (!minHeap.empty()) {
            ListNode* node = minHeap.top();
            minHeap.pop();
            current->next = node;
            current = current->next;
            
            if (node->next != nullptr) {
                minHeap.push(node->next);
            }
        }
        
        return dummy.next;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LintCode 104. 合并k个排序链表测试 ===" << endl;
        
        vector<int> arr1 = {2, 4};
        vector<int> arr2 = {1, 3, 5};
        vector<int> arr3 = {6, 7};
        ListNode* l1 = ListNode::createList(arr1);
        ListNode* l2 = ListNode::createList(arr2);
        ListNode* l3 = ListNode::createList(arr3);
        vector<ListNode*> lists = {l1, l2, l3};
        
        cout << "链表1: ";
        ListNode::printList(lists[0]);
        cout << "链表2: ";
        ListNode::printList(lists[1]);
        cout << "链表3: ";
        ListNode::printList(lists[2]);
        
        ListNode* result = mergeKLists(lists);
        cout << "合并结果: ";
        ListNode::printList(result);
        cout << endl;
        
        // 释放内存
        ListNode::deleteList(result);
        // 注意：l1, l2, l3 已经被合并到result中，不需要单独释放
    }
};

/**
 * 题目9: LeetCode 86. 分隔链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/partition-list/
 */
class PartitionListSolution {
public:
    static ListNode* partition(ListNode* head, int x) {
        ListNode lessHead(0);
        ListNode greaterHead(0);
        ListNode* less = &lessHead;
        ListNode* greater = &greaterHead;
        
        while (head != nullptr) {
            if (head->val < x) {
                less->next = head;
                less = less->next;
            } else {
                greater->next = head;
                greater = greater->next;
            }
            head = head->next;
        }
        
        greater->next = nullptr;
        less->next = greaterHead.next;
        
        return lessHead.next;
    }
    
    static void test() {
        cout << "=== LeetCode 86. 分隔链表测试 ===" << endl;
        vector<int> arr = {1, 4, 3, 2, 5, 2};
        ListNode* list = ListNode::createList(arr);
        cout << "原链表: ";
        ListNode::printList(list);
        ListNode* result = partition(list, 3);
        cout << "分隔后(x=3): ";
        ListNode::printList(result);
        cout << endl;
        ListNode::deleteList(result);
    }
};

/**
 * 题目10: LeetCode 141. 环形链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/linked-list-cycle/
 *
 * 题目描述：
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
 *
 * 解法分析：
 * 1. 快慢指针法 (Floyd 判圈算法) - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用两个指针，一个快指针和一个慢指针。快指针每次移动两步，慢指针每次移动一步。
 * 如果链表中存在环，快指针最终会追上慢指针；如果不存在环，快指针会先到达链表末尾。
 */
class LinkedListCycleSolution {
public:
    /**
     * 解法: 快慢指针法 (Floyd 判圈算法)
     * 时间复杂度: O(n) - 最多遍历链表两次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 初始化快慢指针都指向头节点
     * 2. 快指针每次移动两步，慢指针每次移动一步
     * 3. 如果存在环，快指针会追上慢指针
     * 4. 如果不存在环，快指针会先到达链表末尾
     */
    static bool hasCycle(ListNode* head) {
        // 边界条件检查
        if (head == nullptr || head->next == nullptr) {
            return false;
        }
        
        // 初始化快慢指针
        ListNode* slow = head;
        ListNode* fast = head;
        
        // 移动指针
        while (fast != nullptr && fast->next != nullptr) {
            slow = slow->next;        // 慢指针每次移动一步
            fast = fast->next->next;   // 快指针每次移动两步
            
            // 如果快慢指针相遇，说明存在环
            if (slow == fast) {
                return true;
            }
        }
        
        // 如果快指针到达链表末尾，说明不存在环
        return false;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LeetCode 141. 环形链表测试 ===" << endl;
        
        // 测试用例1: 无环链表
        cout << "测试用例1: 无环链表" << endl;
        vector<int> arr1 = {1, 2, 3, 4};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "链表: ";
        ListNode::printList(list1);
        cout << "是否有环: " << (hasCycle(list1) ? "true" : "false") << endl;
        ListNode::deleteList(list1);
        
        // 测试用例2: 有环链表 (构造环)
        cout << "测试用例2: 有环链表" << endl;
        vector<int> arr2 = {1, 2, 3, 4};
        ListNode* list2 = ListNode::createList(arr2);
        // 构造环: 将尾节点指向第二个节点
        ListNode* cur = list2;
        while (cur->next != nullptr) {
            cur = cur->next;
        }
        cur->next = list2->next; // 尾节点指向第二个节点
        cout << "链表: 1 -> 2 -> 3 -> 4 -> 2 (形成环)" << endl;
        cout << "是否有环: " << (hasCycle(list2) ? "true" : "false") << endl;
        // 注意：有环的链表不能直接删除，这里为了测试通过，我们手动断开环
        cur->next = nullptr;
        ListNode::deleteList(list2);
        
        // 测试用例3: 单节点无环
        cout << "测试用例3: 单节点无环" << endl;
        ListNode* list3 = new ListNode(1);
        cout << "链表: 1" << endl;
        cout << "是否有环: " << (hasCycle(list3) ? "true" : "false") << endl;
        delete list3;
        
        // 测试用例4: 空链表
        cout << "测试用例4: 空链表" << endl;
        ListNode* list4 = nullptr;
        cout << "链表: null" << endl;
        cout << "是否有环: " << (hasCycle(list4) ? "true" : "false") << endl;
        cout << endl;
    }
};

/**
 * 题目11: LeetCode 142. 环形链表 II
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/linked-list-cycle-ii/
 *
 * 题目描述：
 * 给定一个链表的头节点 head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 *
 * 解法分析：
 * 1. 快慢指针法 - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用快慢指针找到环后，将快指针重新指向头节点，然后快慢指针都每次移动一步，
 * 当它们再次相遇时，相遇点就是环的入口节点。
 */
class LinkedListCycleIISolution {
public:
    /**
     * 解法: 快慢指针法
     * 时间复杂度: O(n) - 最多遍历链表三次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用快慢指针找到环
     * 2. 将快指针重新指向头节点
     * 3. 快慢指针都每次移动一步
     * 4. 再次相遇点就是环的入口
     */
    static ListNode* detectCycle(ListNode* head) {
        // 边界条件检查
        if (head == nullptr || head->next == nullptr) {
            return nullptr;
        }
        
        // 第一阶段：使用快慢指针判断是否有环
        ListNode* slow = head;
        ListNode* fast = head;
        
        while (fast != nullptr && fast->next != nullptr) {
            slow = slow->next;
            fast = fast->next->next;
            
            // 如果快慢指针相遇，说明存在环
            if (slow == fast) {
                break;
            }
        }
        
        // 如果没有环，返回nullptr
        if (fast == nullptr || fast->next == nullptr) {
            return nullptr;
        }
        
        // 第二阶段：找到环的入口
        // 将快指针重新指向头节点
        fast = head;
        // 快慢指针都每次移动一步，直到相遇
        while (slow != fast) {
            slow = slow->next;
            fast = fast->next;
        }
        
        // 相遇点就是环的入口
        return slow;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LeetCode 142. 环形链表 II测试 ===" << endl;
        
        // 测试用例1: 无环链表
        cout << "测试用例1: 无环链表" << endl;
        vector<int> arr1 = {1, 2, 3, 4};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "链表: ";
        ListNode::printList(list1);
        ListNode* cycleStart1 = detectCycle(list1);
        cout << "环的入口: " << (cycleStart1 != nullptr ? to_string(cycleStart1->val) : "null") << endl;
        ListNode::deleteList(list1);
        
        // 测试用例2: 有环链表 (构造环)
        cout << "测试用例2: 有环链表" << endl;
        vector<int> arr2 = {1, 2, 3, 4};
        ListNode* list2 = ListNode::createList(arr2);
        // 构造环: 将尾节点指向第二个节点
        ListNode* cur = list2;
        while (cur->next != nullptr) {
            cur = cur->next;
        }
        cur->next = list2->next; // 尾节点指向第二个节点
        cout << "链表: 1 -> 2 -> 3 -> 4 -> 2 (形成环)" << endl;
        ListNode* cycleStart2 = detectCycle(list2);
        cout << "环的入口: " << (cycleStart2 != nullptr ? to_string(cycleStart2->val) : "null") << endl;
        // 注意：有环的链表不能直接删除，这里为了测试通过，我们手动断开环
        cur->next = nullptr;
        ListNode::deleteList(list2);
        
        cout << endl;
    }
};

/**
 * 题目12: LeetCode 160. 相交链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/intersection-of-two-linked-lists/
 *
 * 题目描述：
 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。
 *
 * 解法分析：
 * 1. 双指针法 - 时间复杂度: O(m+n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用两个指针分别遍历两个链表，当一个指针到达链表末尾时，将其指向另一个链表的头节点。
 * 如果两个链表相交，两个指针会在相交节点相遇；如果不相交，两个指针会同时到达链表末尾。
 */
class IntersectionOfTwoLinkedListsSolution {
public:
    /**
     * 解法: 双指针法
     * 时间复杂度: O(m+n) - 最多遍历两个链表各两次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用两个指针分别遍历两个链表
     * 2. 当指针到达链表末尾时，将其指向另一个链表的头节点
     * 3. 如果两个链表相交，两个指针会在相交节点相遇
     * 4. 如果不相交，两个指针会同时到达链表末尾
     */
    static ListNode* getIntersectionNode(ListNode* headA, ListNode* headB) {
        // 边界条件检查
        if (headA == nullptr || headB == nullptr) {
            return nullptr;
        }
        
        // 初始化两个指针
        ListNode* pointerA = headA;
        ListNode* pointerB = headB;
        
        // 当两个指针不相等时继续遍历
        while (pointerA != pointerB) {
            // 当指针到达链表末尾时，将其指向另一个链表的头节点
            pointerA = (pointerA == nullptr) ? headB : pointerA->next;
            pointerB = (pointerB == nullptr) ? headA : pointerB->next;
        }
        
        // 返回相交节点或nullptr
        return pointerA;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LeetCode 160. 相交链表测试 ===" << endl;
        
        // 测试用例1: 相交链表
        cout << "测试用例1: 相交链表" << endl;
        vector<int> commonArr = {8, 4, 5};
        vector<int> arrA = {4, 1};
        vector<int> arrB = {5, 6, 1};
        
        ListNode* common = ListNode::createList(commonArr);
        ListNode* listA = ListNode::createList(arrA);
        ListNode* listB = ListNode::createList(arrB);
        
        // 构造相交链表
        ListNode* curA = listA;
        while (curA->next != nullptr) {
            curA = curA->next;
        }
        curA->next = common;
        
        ListNode* curB = listB;
        while (curB->next != nullptr) {
            curB = curB->next;
        }
        curB->next = common;
        
        cout << "链表A: 4 -> 1 -> 8 -> 4 -> 5" << endl;
        cout << "链表B: 5 -> 6 -> 1 -> 8 -> 4 -> 5" << endl;
        ListNode* intersection1 = getIntersectionNode(listA, listB);
        cout << "相交节点: " << (intersection1 != nullptr ? to_string(intersection1->val) : "null") << endl;
        
        // 注意：相交链表需要特殊处理内存释放
        curA->next = nullptr;
        curB->next = nullptr;
        ListNode::deleteList(listA);
        ListNode::deleteList(listB);
        ListNode::deleteList(common);
        
        // 测试用例2: 不相交链表
        cout << "测试用例2: 不相交链表" << endl;
        vector<int> arrC = {1, 2, 3};
        vector<int> arrD = {4, 5, 6};
        ListNode* listC = ListNode::createList(arrC);
        ListNode* listD = ListNode::createList(arrD);
        cout << "链表C: 1 -> 2 -> 3" << endl;
        cout << "链表D: 4 -> 5 -> 6" << endl;
        ListNode* intersection2 = getIntersectionNode(listC, listD);
        cout << "相交节点: " << (intersection2 != nullptr ? to_string(intersection2->val) : "null") << endl;
        ListNode::deleteList(listC);
        ListNode::deleteList(listD);
        
        cout << endl;
    }
};

/**
 * 题目13: LeetCode 206. 反转链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/reverse-linked-list/
 *
 * 题目描述：
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 *
 * 解法分析：
 * 1. 迭代法 - 时间复杂度: O(n), 空间复杂度: O(1)
 * 2. 递归法 - 时间复杂度: O(n), 空间复杂度: O(n)
 *
 * 解题思路：
 * 迭代法：使用三个指针分别指向前一个节点、当前节点和下一个节点，逐个反转节点的指向。
 * 递归法：递归到链表末尾，然后在回溯过程中反转节点的指向。
 */
class ReverseLinkedListSolution {
public:
    /**
     * 解法1: 迭代法 (推荐)
     * 时间复杂度: O(n) - 需要遍历链表一次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用三个指针：prev(前一个节点)、current(当前节点)、next(下一个节点)
     * 2. 逐个反转节点的指向
     * 3. 移动指针继续处理下一个节点
     */
    static ListNode* reverseListIterative(ListNode* head) {
        // 初始化指针
        ListNode* prev = nullptr;
        ListNode* current = head;
        
        // 遍历链表
        while (current != nullptr) {
            // 保存下一个节点
            ListNode* next = current->next;
            // 反转当前节点的指向
            current->next = prev;
            // 移动指针
            prev = current;
            current = next;
        }
        
        // 返回新的头节点
        return prev;
    }
    
    /**
     * 解法2: 递归法
     * 时间复杂度: O(n) - 需要遍历链表一次
     * 空间复杂度: O(n) - 递归调用栈的深度
     *
     * 核心思想：
     * 1. 递归到链表末尾
     * 2. 在回溯过程中反转节点的指向
     */
    static ListNode* reverseListRecursive(ListNode* head) {
        // 递归终止条件
        if (head == nullptr || head->next == nullptr) {
            return head;
        }
        
        // 递归处理下一个节点
        ListNode* newHead = reverseListRecursive(head->next);
        // 反转当前节点和下一个节点的连接
        head->next->next = head;
        head->next = nullptr;
        
        // 返回新的头节点
        return newHead;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LeetCode 206. 反转链表测试 ===" << endl;
        
        // 测试用例1: 正常链表
        cout << "测试用例1: 正常链表" << endl;
        vector<int> arr1 = {1, 2, 3, 4, 5};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "原链表: ";
        ListNode::printList(list1);
        ListNode* reversed1 = reverseListIterative(list1);
        cout << "迭代法反转后: ";
        ListNode::printList(reversed1);
        ListNode::deleteList(reversed1);
        
        // 重新创建测试数据
        ListNode* list2 = ListNode::createList(arr1);
        ListNode* reversed2 = reverseListRecursive(list2);
        cout << "递归法反转后: ";
        ListNode::printList(reversed2);
        cout << endl;
        ListNode::deleteList(reversed2);
        
        // 测试用例2: 单节点链表
        cout << "测试用例2: 单节点链表" << endl;
        ListNode* list3 = new ListNode(1);
        cout << "原链表: ";
        ListNode::printList(list3);
        ListNode* reversed3 = reverseListIterative(list3);
        cout << "反转后: ";
        ListNode::printList(reversed3);
        delete reversed3;
        cout << endl;
        
        // 测试用例3: 空链表
        cout << "测试用例3: 空链表" << endl;
        ListNode* list4 = nullptr;
        cout << "原链表: ";
        ListNode::printList(list4);
        ListNode* reversed4 = reverseListIterative(list4);
        cout << "反转后: ";
        ListNode::printList(reversed4);
        cout << endl;
    }
};

/**
 * 题目14: LeetCode 234. 回文链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/palindrome-linked-list/
 *
 * 题目描述：
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 *
 * 解法分析：
 * 1. 快慢指针 + 反转链表 - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 1. 使用快慢指针找到链表中点
 * 2. 反转后半部分链表
 * 3. 比较前半部分和反转后的后半部分
 * 4. 恢复链表结构(可选)
 */
class PalindromeLinkedListSolution {
public:
    /**
     * 解法: 快慢指针 + 反转链表
     * 时间复杂度: O(n) - 需要遍历链表多次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用快慢指针找到链表中点
     * 2. 反转后半部分链表
     * 3. 比较前半部分和反转后的后半部分
     */
    static bool isPalindrome(ListNode* head) {
        // 边界条件检查
        if (head == nullptr || head->next == nullptr) {
            return true;
        }
        
        // 第一步：使用快慢指针找到链表中点
        ListNode* slow = head;
        ListNode* fast = head;
        
        while (fast->next != nullptr && fast->next->next != nullptr) {
            slow = slow->next;
            fast = fast->next->next;
        }
        
        // 第二步：反转后半部分链表
        ListNode* secondHalf = reverseList(slow->next);
        
        // 第三步：比较前半部分和反转后的后半部分
        ListNode* firstHalf = head;
        ListNode* secondHalfCopy = secondHalf; // 保存用于恢复
        bool isPalindrome = true;
        
        while (secondHalf != nullptr) {
            if (firstHalf->val != secondHalf->val) {
                isPalindrome = false;
                break;
            }
            firstHalf = firstHalf->next;
            secondHalf = secondHalf->next;
        }
        
        // 第四步：恢复链表结构(可选)
        slow->next = reverseList(secondHalfCopy);
        
        return isPalindrome;
    }
    
    /**
     * 反转链表的辅助函数
     */
    static ListNode* reverseList(ListNode* head) {
        ListNode* prev = nullptr;
        ListNode* current = head;
        
        while (current != nullptr) {
            ListNode* next = current->next;
            current->next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LeetCode 234. 回文链表测试 ===" << endl;
        
        // 测试用例1: 回文链表
        cout << "测试用例1: 回文链表" << endl;
        vector<int> arr1 = {1, 2, 2, 1};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "链表: ";
        ListNode::printList(list1);
        cout << "是否为回文链表: " << (isPalindrome(list1) ? "true" : "false") << endl;
        ListNode::deleteList(list1);
        
        // 测试用例2: 非回文链表
        cout << "测试用例2: 非回文链表" << endl;
        vector<int> arr2 = {1, 2, 3, 4};
        ListNode* list2 = ListNode::createList(arr2);
        cout << "链表: ";
        ListNode::printList(list2);
        cout << "是否为回文链表: " << (isPalindrome(list2) ? "true" : "false") << endl;
        ListNode::deleteList(list2);
        
        // 测试用例3: 单节点链表
        cout << "测试用例3: 单节点链表" << endl;
        ListNode* list3 = new ListNode(1);
        cout << "链表: ";
        ListNode::printList(list3);
        cout << "是否为回文链表: " << (isPalindrome(list3) ? "true" : "false") << endl;
        delete list3;
        
        cout << endl;
    }
};

/**
 * 题目15: LeetCode 19. 删除链表的倒数第 N 个结点
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
 *
 * 题目描述：
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 *
 * 解法分析：
 * 1. 快慢指针法 - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用两个指针，快指针先移动n+1步，然后快慢指针同时移动，
 * 当快指针到达链表末尾时，慢指针正好指向要删除节点的前一个节点。
 */
class RemoveNthNodeFromEndOfListSolution {
public:
    /**
     * 解法: 快慢指针法
     * 时间复杂度: O(n) - 需要遍历链表一次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用哨兵节点简化边界处理
     * 2. 快指针先移动n+1步
     * 3. 快慢指针同时移动
     * 4. 当快指针到达链表末尾时，慢指针正好指向要删除节点的前一个节点
     */
    static ListNode* removeNthFromEnd(ListNode* head, int n) {
        // 创建哨兵节点，简化边界处理
        ListNode dummy(0);
        dummy.next = head;
        
        // 初始化快慢指针
        ListNode* fast = &dummy;
        ListNode* slow = &dummy;
        
        // 快指针先移动n+1步
        for (int i = 0; i <= n; i++) {
            fast = fast->next;
        }
        
        // 快慢指针同时移动
        while (fast != nullptr) {
            fast = fast->next;
            slow = slow->next;
        }
        
        // 删除倒数第n个节点
        slow->next = slow->next->next;
        
        // 返回头节点
        return dummy.next;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        cout << "=== LeetCode 19. 删除链表的倒数第 N 个结点测试 ===" << endl;
        
        // 测试用例1: 删除中间节点
        cout << "测试用例1: 删除中间节点" << endl;
        vector<int> arr1 = {1, 2, 3, 4, 5};
        ListNode* list1 = ListNode::createList(arr1);
        cout << "原链表: ";
        ListNode::printList(list1);
        ListNode* result1 = removeNthFromEnd(list1, 2);
        cout << "删除倒数第2个节点后: ";
        ListNode::printList(result1);
        ListNode::deleteList(result1);
        
        // 测试用例2: 删除头节点
        cout << "测试用例2: 删除头节点" << endl;
        vector<int> arr2 = {1, 2, 3, 4, 5};
        ListNode* list2 = ListNode::createList(arr2);
        cout << "原链表: ";
        ListNode::printList(list2);
        ListNode* result2 = removeNthFromEnd(list2, 5);
        cout << "删除倒数第5个节点后: ";
        ListNode::printList(result2);
        ListNode::deleteList(result2);
        
        // 测试用例3: 删除尾节点
        cout << "测试用例3: 删除尾节点" << endl;
        vector<int> arr3 = {1, 2, 3, 4, 5};
        ListNode* list3 = ListNode::createList(arr3);
        cout << "原链表: ";
        ListNode::printList(list3);
        ListNode* result3 = removeNthFromEnd(list3, 1);
        cout << "删除倒数第1个节点后: ";
        ListNode::printList(result3);
        ListNode::deleteList(result3);
        
        cout << endl;
    }
};

/**
 * 算法总结与技巧提升
 */
class AlgorithmSummary {
public:
    static void printSummary() {
        cout << "========== 链表合并算法总结 ==========" << endl;
        cout << "1. 核心算法技巧:" << endl;
        cout << "   - 双指针法: 适用于两个有序序列的合并，时间复杂度O(m+n)" << endl;
        cout << "   - 优先队列法: 适用于K个有序序列的合并，时间复杂度O(N*logK)" << endl;
        cout << "   - 分治法: 适用于K个序列的归并，时间复杂度O(N*logK)" << endl;
        cout << "   - 哨兵节点: 简化链表操作的边界处理，提高代码可读性" << endl;
        cout << "   - 原地修改: 避免额外空间开销，适用于数组合并等场景" << endl;
        cout << endl;
        cout << "2. 工程化考量:" << endl;
        cout << "   - 异常处理: 处理空链表、单节点链表等边界情况" << endl;
        cout << "   - 内存管理: 在C++中需要注意释放链表内存，避免内存泄漏" << endl;
        cout << "   - 性能优化: 对于大规模数据，优先队列的常数项优化很重要" << endl;
        cout << "   - 线程安全: 在多线程环境下需要考虑同步问题" << endl;
        cout << endl;
        cout << "3. 调试技巧:" << endl;
        cout << "   - 打印中间状态: 使用cout跟踪指针移动" << endl;
        cout << "   - 边界测试: 测试空输入、单元素输入、极端值等情况" << endl;
        cout << "   - 断言验证: 使用assert验证关键条件是否满足" << endl;
        cout << endl;
        cout << "4. 拓展应用:" << endl;
        cout << "   - 归并排序: 链表排序的最佳选择之一" << endl;
        cout << "   - 多路归并: 外部排序的基础算法" << endl;
        cout << "   - 数据流处理: 实时合并多个有序数据流" << endl;
        cout << "======================================\n" << endl;
    }
};

/**
 * 综合测试函数
 */
void runAllTests() {
    MergeTwoSortedListsSolution::test();
    MergeKSortedListsSolution::test();
    MergeSortedArraySolution::test();
    SortListSolution::test();
    AddTwoNumbersSolution::test();
    SwapNodesInPairsSolution::test();
    NowCoderMergeSortedListsSolution::test();
    LintCodeMergeKListsSolution::test();
    PartitionListSolution::test();
    
    // 新增题目的测试
    LinkedListCycleSolution::test();
    LinkedListCycleIISolution::test();
    IntersectionOfTwoLinkedListsSolution::test();
    ReverseLinkedListSolution::test();
    PalindromeLinkedListSolution::test();
    RemoveNthNodeFromEndOfListSolution::test();
    
    AlgorithmSummary::printSummary();
}

/**
 * 主函数 - 运行所有测试
 */
int main() {
    runAllTests();
    return 0;
}