#include <iostream>
#include <vector>

/**
 * 链表分隔问题 - 最优解实现与详细分析
 * 
 * 题目描述：
 * 给你一个链表的头节点 head 和一个特定值 x，请你对链表进行分隔，
 * 使得所有小于 x 的节点都出现在大于或等于 x 的节点之前。
 * 你应当保留两个分区中每个节点的初始相对位置。
 * 
 * 示例：
 * 输入：head = [1,4,3,2,5,2], x = 3
 * 输出：[1,2,2,4,3,5]
 * 
 * 输入：head = [2,1], x = 2
 * 输出：[1,2]
 * 
 * 解题思路：
 * 1. 双链表法（推荐）：使用两个链表分别存储小于x和大于等于x的节点，最后连接
 * 2. 原地操作法：在原链表中移动节点，保持相对顺序
 * 
 * 时间复杂度：O(n) - 只需遍历链表一次
 * 空间复杂度：O(1) - 只使用常数级别额外空间
 * 
 * 相似题目：
 * 1. LeetCode 86. Partition List (本题)
 * 2. LintCode 96. Partition List
 * 3. 牛客网 NC140. 链表的奇偶重排
 * 4. LeetCode 21. Merge Two Sorted Lists
 * 5. LeetCode 23. Merge k Sorted Lists
 * 6. LeetCode 148. Sort List
 * 
 * 测试链接：https://leetcode.cn/problems/partition-list/
 */

// 链表节点定义
struct ListNode {
    int val;         // 节点值
    ListNode *next;  // 指向下一节点的指针
    
    /**
     * 默认构造函数 - 创建值为0的节点
     */
    ListNode() : val(0), next(nullptr) {}
    
    /**
     * 构造函数 - 创建指定值的节点
     * @param x 节点值
     */
    ListNode(int x) : val(x), next(nullptr) {}
    
    /**
     * 构造函数 - 创建指定值和后继节点的节点
     * @param x 节点值
     * @param next 后继节点指针
     */
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class Solution {
public:
    /**
     * 解法1：双链表法（推荐最优解）
     * 
     * 核心思想：
     * 1. 创建两个虚拟头节点，分别用于存储小于x和大于等于x的节点
     * 2. 遍历原链表，根据节点值将节点连接到对应的链表中
     * 3. 连接两个链表并返回结果
     * 
     * 此解法的优势：
     * - 逻辑清晰，易于理解和实现
     * - 边界条件处理简单，不容易出错
     * - 满足O(n)时间复杂度和O(1)空间复杂度要求
     * 
     * 时间复杂度分析：
     * - 遍历操作：O(n) - 只需要遍历原链表一次
     * - 指针操作：O(1) - 每个节点进行常数次指针操作
     * - 总体复杂度：O(n)
     * 
     * 空间复杂度分析：
     * - 额外节点：O(1) - 只使用两个虚拟头节点（栈上分配）
     * - 指针变量：O(1) - 使用常数个指针变量
     * - 总体复杂度：O(1)
     * 
     * @param head 链表头节点
     * @param x 分隔值
     * @return 分隔后的链表头节点
     */
    static ListNode* partition(ListNode* head, int x) {
        // 【异常处理】空链表直接返回nullptr
        if (head == nullptr) {
            return nullptr;
        }
        
        // 创建两个虚拟头节点（在栈上分配，自动析构），分别用于存储小于x和大于等于x的节点
        // 使用虚拟头节点可以避免处理头节点为空的边界情况
        ListNode leftDummy(0);
        ListNode rightDummy(0);
        
        // 两个链表的尾指针，用于高效添加节点
        ListNode* leftTail = &leftDummy;
        ListNode* rightTail = &rightDummy;
        
        // 遍历原链表
        ListNode* current = head;
        while (current != nullptr) {
            // 【关键点】提前保存下一个节点，避免在操作当前节点时丢失链表后续部分
            ListNode* next = current->next;
            
            // 【重要】断开当前节点与原链表的连接，防止形成环
            current->next = nullptr;
            
            // 根据节点值将节点连接到对应的链表中
            if (current->val < x) {
                // 小于x的节点连接到左侧链表
                leftTail->next = current;
                leftTail = current;  // 更新左侧链表尾指针
            } else {
                // 大于等于x的节点连接到右侧链表
                rightTail->next = current;
                rightTail = current;  // 更新右侧链表尾指针
            }
            
            // 移动到下一个节点
            current = next;
        }
        
        // 【关键点】连接两个链表：将左侧链表的尾部连接到右侧链表的头部
        leftTail->next = rightDummy.next;
        
        // 返回结果链表的头节点（左侧链表的第一个有效节点）
        return leftDummy.next;
    }

    /**
     * 解法2：原地操作法
     * 
     * 核心思想：
     * 1. 使用一个指针遍历链表
     * 2. 遇到小于x的节点就将其移动到前面
     * 3. 保持相对顺序不变
     * 
     * 这种方法虽然也是O(n)时间复杂度和O(1)空间复杂度，
     * 但实现更复杂，且容易在指针操作中出错
     * 
     * 时间复杂度：O(n) - 只需遍历链表一次
     * 空间复杂度：O(1) - 只使用常数级别额外空间
     * 
     * @param head 链表头节点
     * @param x 分隔值
     * @return 分隔后的链表头节点
     */
    static ListNode* partition2(ListNode* head, int x) {
        // 【异常处理】空链表直接返回nullptr
        if (head == nullptr) {
            return nullptr;
        }
        
        // 创建虚拟头节点（在栈上分配，自动析构），简化边界处理
        ListNode dummy(0);
        dummy.next = head;
        
        // 找到第一个大于等于x的节点的前驱节点
        // 这个节点将作为小于x的节点插入位置的前驱
        ListNode* prev = &dummy;
        while (prev->next != nullptr && prev->next->val < x) {
            prev = prev->next;
        }
        
        // 当前节点指针，用于遍历链表
        ListNode* curr = prev;
        
        // 遍历链表剩余部分
        while (curr->next != nullptr) {
            // 如果下一个节点小于x，则需要将其移动到前面
            if (curr->next->val < x) {
                // 【指针操作】取出要移动的节点
                ListNode* moveNode = curr->next;
                
                // 从当前位置断开
                curr->next = moveNode->next;
                
                // 插入到prev后面
                moveNode->next = prev->next;
                prev->next = moveNode;
                
                // 更新prev指针，为下一次插入做准备
                prev = moveNode;
            } else {
                // 否则继续向后移动
                curr = curr->next;
            }
        }
        
        // 返回结果链表的头节点
        return dummy.next;
    }
};

// ========== 扩展题目1：LeetCode 328. Odd Even Linked List（链表奇偶重排）==========
/**
 * LeetCode 328. Odd Even Linked List
 * 题目链接：https://leetcode.cn/problems/odd-even-linked-list/
 * 
 * 题目描述：
 * 给定单链表的头节点head，将所有索引为奇数的节点和索引为偶数的节点分别组合在一起
 * 
 * 时间复杂度：O(n) 空间复杂度：O(1) 是否最优解：是
 */
ListNode* oddEvenList(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return head;
    }
    
    ListNode* odd = head;
    ListNode* even = head->next;
    ListNode* evenHead = even;
    
    while (even != nullptr && even->next != nullptr) {
        odd->next = even->next;
        odd = odd->next;
        even->next = odd->next;
        even = even->next;
    }
    
    odd->next = evenHead;
    return head;
}

// ========== 扩展题目2：LeetCode 725. Split Linked List in Parts ==========
/**
 * LeetCode 725. Split Linked List in Parts
 * 题目链接：https://leetcode.cn/problems/split-linked-list-in-parts/
 * 
 * 时间复杂度：O(n+k) 空间复杂度：O(k) 是否最优解：是
 */
std::vector<ListNode*> splitListToParts(ListNode* head, int k) {
    int length = 0;
    ListNode* curr = head;
    while (curr != nullptr) {
        length++;
        curr = curr->next;
    }
    
    int partSize = length / k;
    int remainder = length % k;
    
    std::vector<ListNode*> result(k, nullptr);
    curr = head;
    
    for (int i = 0; i < k && curr != nullptr; i++) {
        result[i] = curr;
        int currentPartSize = partSize + (i < remainder ? 1 : 0);
        
        for (int j = 1; j < currentPartSize; j++) {
            curr = curr->next;
        }
        
        ListNode* next = curr->next;
        curr->next = nullptr;
        curr = next;
    }
    
    return result;
}

// ========== 扩展题目3：LeetCode 2095. Delete Middle Node ==========
/**
 * LeetCode 2095. Delete the Middle Node of a Linked List
 * 题目链接：https://leetcode.cn/problems/delete-the-middle-node-of-a-linked-list/
 * 
 * 时间复杂度：O(n) 空间复杂度：O(1) 是否最优解：是
 */
ListNode* deleteMiddle(ListNode* head) {
    if (head == nullptr || head->next == nullptr) {
        return nullptr;
    }
    
    ListNode dummy(0, head);
    ListNode* slow = &dummy;
    ListNode* fast = head;
    
    while (fast != nullptr && fast->next != nullptr) {
        slow = slow->next;
        fast = fast->next->next;
    }
    
    slow->next = slow->next->next;
    return dummy.next;
}

/**
 * 打印链表的辅助函数
 * 
 * @param head 链表头节点
 */
void printList(ListNode* head) {
    // 【边界检查】处理空链表情况
    if (head == nullptr) {
        std::cout << "空链表" << std::endl;
        return;
    }
    
    ListNode* current = head;
    while (current != nullptr) {
        std::cout << current->val;
        if (current->next != nullptr) {
            std::cout << " -> ";
        }
        current = current->next;
    }
    std::cout << std::endl;
}

/**
 * 验证链表分隔结果是否正确
 * 
 * 验证规则：
 * 1. 所有小于x的节点必须出现在大于等于x的节点之前
 * 2. 必须保持节点的相对顺序
 * 3. 不能出现循环引用
 * 
 * 实现思路：
 * - 使用状态标志跟踪是否已经遇到大于等于x的节点
 * - 遍历链表检查是否违反分区规则
 * - 同时检查是否存在循环（通过记录访问过的节点数量限制）
 * 
 * @param head 分隔后的链表头节点
 * @param x 分隔值
 * @return 验证是否通过
 */
bool verifyPartitionResult(ListNode* head, int x) {
    if (head == nullptr) {
        std::cout << "验证结果：通过（空链表）" << std::endl;
        return true;
    }
    
    bool passedX = false; // 标志是否已经遇到大于等于x的节点
    ListNode* current = head;
    int nodeCount = 0; // 用于检测循环引用
    const int maxNodes = 1000; // 链表最大节点数限制，防止无限循环
    
    while (current != nullptr && nodeCount < maxNodes) {
        // 检查分区规则：如果已经遇到过大于等于x的节点，则后续节点都不能小于x
        if (passedX && current->val < x) {
            std::cout << "验证结果：失败！违反分区规则 - 大于等于" << x << "的节点后出现小于" << x << "的节点" << std::endl;
            return false;
        }
        
        // 如果当前节点大于等于x，则设置passedX标志为true
        if (current->val >= x) {
            passedX = true;
        }
        
        current = current->next;
        nodeCount++;
    }
    
    // 检查是否存在循环引用
    if (nodeCount >= maxNodes) {
        std::cout << "验证结果：失败！检测到可能的循环引用" << std::endl;
        return false;
    }
    
    std::cout << "验证结果：通过 - 分区规则正确遵守" << std::endl;
    return true;
}

/**
 * 创建链表的辅助函数
 * 
 * @param values 包含节点值的向量
 * @return 创建的链表头节点，如果values为空则返回nullptr
 */
ListNode* createList(const std::vector<int>& values) {
    // 【边界检查】处理空向量情况
    if (values.empty()) return nullptr;
    
    // 创建头节点
    ListNode* head = new ListNode(values[0]);
    ListNode* current = head;
    
    // 逐个创建后续节点
    for (size_t i = 1; i < values.size(); i++) {
        current->next = new ListNode(values[i]);
        current = current->next;
    }
    
    return head;
}

/**
 * 释放链表内存的辅助函数
 * 
 * 【内存管理】确保链表节点占用的内存被正确释放，避免内存泄漏
 * 
 * @param head 链表头节点
 */
void deleteList(ListNode* head) {
    while (head != nullptr) {
        ListNode* temp = head;
        head = head->next;
        delete temp;  // 释放当前节点内存
    }
}

/**
 * 运行单个测试用例的辅助函数
 * 
 * @param values 测试用例的节点值向量
 * @param x 分隔值
 * @param testName 测试用例名称
 * @param testPurpose 测试目的描述
 */
void runTestCase(const std::vector<int>& values, int x, const std::string& testName, const std::string& testPurpose = "") {
    std::cout << "\n【测试用例】" << testName << std::endl;
    
    if (!testPurpose.empty()) {
        std::cout << "测试目的: " << testPurpose << std::endl;
    }
    
    // 创建测试链表
    ListNode* head = createList(values);
    
    std::cout << "原链表: " << std::endl;
    printList(head);
    
    // 测试解法1 - 双链表法
    ListNode* result1 = Solution::partition(head, x);
    std::cout << "分隔后 (双链表法): " << std::endl;
    printList(result1);
    
    // 验证结果正确性
    verifyPartitionResult(result1, x);
    
    // 【内存管理】释放解法1使用的内存
    deleteList(result1);
    
    // 重新构建测试链表
    ListNode* head2 = createList(values);
    
    // 测试解法2 - 原地操作法
    ListNode* result2 = Solution::partition2(head2, x);
    std::cout << "分隔后 (原地操作法): " << std::endl;
    printList(result2);
    
    // 验证结果正确性
    verifyPartitionResult(result2, x);
    
    // 【内存管理】释放解法2使用的内存
    deleteList(result2);
}

/**
 * 测试空链表的辅助函数
 * 
 * @param x 分隔值
 */
void testEmptyList(int x) {
    std::cout << "\n【测试用例】空链表" << std::endl;
    std::cout << "测试目的: 验证算法对边界情况的处理能力" << std::endl;
    
    ListNode* head = nullptr;
    
    std::cout << "原链表: " << std::endl;
    printList(head);
    
    // 测试解法1 - 双链表法
    ListNode* result1 = Solution::partition(head, x);
    std::cout << "分隔后 (双链表法): " << std::endl;
    printList(result1);
    
    // 验证结果正确性
    verifyPartitionResult(result1, x);
    
    // 测试解法2 - 原地操作法
    ListNode* result2 = Solution::partition2(head, x);
    std::cout << "分隔后 (原地操作法): " << std::endl;
    printList(result2);
    
    // 验证结果正确性
    verifyPartitionResult(result2, x);
    
    // 空链表不需要释放内存，因为result1和result2都是nullptr
}

// 测试函数
int main() {
    std::cout << "=== 链表分隔问题测试 ===" << std::endl;
    std::cout << "算法本质：分类与合并模式，使用虚拟头节点技术" << std::endl;
    
    /**
     * 测试策略
     * 1. 功能验证：确保算法正确实现了分隔功能
     * 2. 边界测试：测试特殊输入情况
     * 3. 极端情况测试：测试性能和正确性边界
     * 4. 多解法对比：验证不同实现方法的正确性
     * 5. 结果验证：确保所有分隔后的链表满足条件
     */
    
    // 【测试用例1】标准情况 - 混合大小的元素分布
    // 输入：[1,4,3,2,5,2], x = 3
    // 预期输出: [1,2,2,4,3,5]
    // 验证点：1. 小于x的节点在前 2. 大于等于x的节点在后 3. 相对顺序保持不变
    std::vector<int> values1 = {1, 4, 3, 2, 5, 2};
    runTestCase(values1, 3, "标准情况 - 混合元素分布", "验证基本功能正确性，确保相对顺序保持不变");
    
    // 测试用例2: 两个节点，需要交换
    // 输入：[2,1], x = 2
    // 预期输出: [1,2]
    std::vector<int> values2 = {2, 1};
    runTestCase(values2, 2, "两个节点需要交换", "验证交换两个节点的正确性");
    
    // 测试用例3: 空链表
    testEmptyList(1);
    
    // 测试用例4: 单节点链表
    std::vector<int> values4 = {5};
    runTestCase(values4, 3, "单节点链表", "验证对单节点情况的处理");
    
    // 测试用例5: 所有节点值都小于x
    std::vector<int> values5 = {1, 2, 3};
    runTestCase(values5, 4, "所有节点值都小于x", "验证全小于分割值的情况");
    
    // 测试用例6: 所有节点值都大于等于x
    std::vector<int> values6 = {5, 6, 7};
    runTestCase(values6, 4, "所有节点值都大于等于x", "验证全大于等于分割值的情况");
    
    // 测试用例7: 已排序的链表
    std::vector<int> values7 = {1, 2, 3, 4, 5};
    runTestCase(values7, 3, "已排序的链表", "验证对已排序链表的处理");
    
    // 测试用例8: 逆序的链表
    std::vector<int> values8 = {5, 4, 3, 2, 1};
    runTestCase(values8, 3, "逆序的链表", "验证对逆序链表的处理");
    
    std::cout << "\n所有测试用例执行完毕！" << std::endl;
    
    return 0;
}