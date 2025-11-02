// 环形链表II - LeetCode 142
// 测试链接: https://leetcode.cn/problems/linked-list-cycle-ii/
#include <iostream>
#include <vector>
#include <unordered_set>
using namespace std;

// 定义链表节点结构
struct ListNode {
    int val;
    ListNode *next;
    ListNode() : val(0), next(nullptr) {}
    ListNode(int x) : val(x), next(nullptr) {}
    ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class Solution {
public:
    // 方法1: Floyd的龟兔赛跑算法（快慢指针）
    ListNode* detectCycle(ListNode* head) {
        // 边界情况处理
        if (!head || !head->next) {
            return nullptr; // 空链表或单节点链表，不可能有环
        }
        
        // 第一步：使用快慢指针找到相遇点
        ListNode* slow = head;
        ListNode* fast = head;
        bool hasCycle = false;
        
        while (fast && fast->next) {
            slow = slow->next;           // 慢指针每次移动一步
            fast = fast->next->next;     // 快指针每次移动两步
            
            if (slow == fast) {          // 快慢指针相遇，说明有环
                hasCycle = true;
                break;
            }
        }
        
        // 如果没有环，直接返回nullptr
        if (!hasCycle) {
            return nullptr;
        }
        
        // 第二步：从相遇点和头节点同时出发，相遇点即为环的入口
        slow = head;                  // 重置慢指针到头节点
        while (slow != fast) {        // 两个指针每次都移动一步
            slow = slow->next;
            fast = fast->next;
        }
        
        return slow;                  // 返回环的入口节点
    }
    
    // 方法2: 使用哈希表记录访问过的节点
    ListNode* detectCycleHashTable(ListNode* head) {
        unordered_set<ListNode*> visited; // 用于存储已经访问过的节点
        
        ListNode* curr = head;
        while (curr) {
            // 如果当前节点已经在哈希表中，说明找到了环的入口
            if (visited.count(curr)) {
                return curr;
            }
            // 将当前节点添加到哈希表中
            visited.insert(curr);
            // 移动到下一个节点
            curr = curr->next;
        }
        
        // 遍历完链表没有发现环
        return nullptr;
    }
    
    // 方法3: 标记法 - 修改节点值或标志位（仅用于演示，不推荐在实际应用中使用，因为会修改原链表）
    ListNode* detectCycleMarking(ListNode* head) {
        ListNode* curr = head;
        
        // 使用一个特殊值（在实际应用中可能需要检查这个值是否在合法范围内）
        const int MARKER = INT_MIN; // 使用最小整数值作为标记
        
        while (curr) {
            // 如果当前节点的值已经被标记，说明找到了环的入口
            if (curr->val == MARKER) {
                // 恢复原链表（可选，但为了不影响原链表，最好恢复）
                // 这里省略恢复代码
                return curr;
            }
            
            // 标记当前节点
            curr->val = MARKER;
            // 移动到下一个节点
            curr = curr->next;
        }
        
        return nullptr;
    }
    
    // 方法4: 计数法 - 对于每个节点，遍历后续节点看是否回到自身（暴力解法）
    ListNode* detectCycleBruteForce(ListNode* head) {
        if (!head || !head->next) {
            return nullptr;
        }
        
        ListNode* curr = head;
        int index = 0; // 当前节点的索引
        
        while (curr) {
            // 对于每个节点，从头开始遍历看是否回到自身
            ListNode* temp = head;
            for (int i = 0; i < index; i++) {
                if (temp == curr->next) {
                    return temp;
                }
                temp = temp->next;
            }
            
            curr = curr->next;
            index++;
        }
        
        return nullptr;
    }
};

// 辅助函数：构建带有环的链表
ListNode* buildCyclicList(vector<int>& nums, int pos) {
    int n = nums.size();
    if (n == 0) return nullptr;
    
    // 创建所有节点
    vector<ListNode*> nodeList;
    for (int num : nums) {
        nodeList.push_back(new ListNode(num));
    }
    
    // 连接节点
    for (int i = 0; i < n - 1; i++) {
        nodeList[i]->next = nodeList[i + 1];
    }
    
    // 创建环（如果pos >= 0）
    if (pos >= 0 && pos < n) {
        nodeList[n - 1]->next = nodeList[pos];
    }
    
    return nodeList[0];
}

// 辅助函数：构建无环链表
ListNode* buildList(vector<int>& nums) {
    return buildCyclicList(nums, -1);
}

// 辅助函数：打印链表（最多打印n个节点，避免在有环的情况下无限循环）
void printList(ListNode* head, int maxNodes = 20) {
    int count = 0;
    while (head && count < maxNodes) {
        cout << head->val;
        if (head->next && count < maxNodes - 1) {
            cout << " -> ";
        }
        head = head->next;
        count++;
    }
    if (count == maxNodes) {
        cout << " -> ... (detected potential cycle)";
    }
    cout << endl;
}

// 辅助函数：释放链表内存（对于有环链表，需要特殊处理避免无限循环）
void freeList(ListNode* head, bool hasCycle = false) {
    if (!head) return;
    
    if (hasCycle) {
        // 对于有环链表，需要先找到环的入口，断开环，然后释放
        Solution solution;
        ListNode* cycleEntry = solution.detectCycleHashTable(head);
        
        if (cycleEntry) {
            // 找到环的最后一个节点
            ListNode* lastNode = cycleEntry;
            while (lastNode->next != cycleEntry) {
                lastNode = lastNode->next;
            }
            // 断开环
            lastNode->next = nullptr;
        }
    }
    
    // 释放链表
    while (head) {
        ListNode* temp = head;
        head = head->next;
        delete temp;
    }
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: [3,2,0,-4], pos = 1
    vector<int> nums1 = {3, 2, 0, -4};
    int pos1 = 1;
    ListNode* head1 = buildCyclicList(nums1, pos1);
    
    cout << "测试用例1: [3,2,0,-4], pos = 1（环从索引1开始）\n链表内容: ";
    printList(head1);
    
    ListNode* result1 = solution.detectCycle(head1);
    cout << "方法1（快慢指针）结果: ";
    if (result1) {
        cout << "环的入口节点值: " << result1->val << endl;
    } else {
        cout << "无环" << endl;
    }
    
    ListNode* result1_hash = solution.detectCycleHashTable(head1);
    cout << "方法2（哈希表）结果: ";
    if (result1_hash) {
        cout << "环的入口节点值: " << result1_hash->val << endl;
    } else {
        cout << "无环" << endl;
    }
    
    // 测试用例2: [1,2], pos = 0
    vector<int> nums2 = {1, 2};
    int pos2 = 0;
    ListNode* head2 = buildCyclicList(nums2, pos2);
    
    cout << "\n测试用例2: [1,2], pos = 0（环从头节点开始）\n链表内容: ";
    printList(head2);
    
    ListNode* result2 = solution.detectCycle(head2);
    cout << "方法1（快慢指针）结果: ";
    if (result2) {
        cout << "环的入口节点值: " << result2->val << endl;
    } else {
        cout << "无环" << endl;
    }
    
    // 测试用例3: [1], pos = -1（无环）
    vector<int> nums3 = {1};
    ListNode* head3 = buildList(nums3);
    
    cout << "\n测试用例3: [1], pos = -1（无环）\n链表内容: ";
    printList(head3);
    
    ListNode* result3 = solution.detectCycle(head3);
    cout << "方法1（快慢指针）结果: ";
    if (result3) {
        cout << "环的入口节点值: " << result3->val << endl;
    } else {
        cout << "无环" << endl;
    }
    
    // 测试用例4: [1,2,3,4,5], pos = 2
    vector<int> nums4 = {1, 2, 3, 4, 5};
    int pos4 = 2;
    ListNode* head4 = buildCyclicList(nums4, pos4);
    
    cout << "\n测试用例4: [1,2,3,4,5], pos = 2（环从索引2开始）\n链表内容: ";
    printList(head4);
    
    ListNode* result4 = solution.detectCycle(head4);
    cout << "方法1（快慢指针）结果: ";
    if (result4) {
        cout << "环的入口节点值: " << result4->val << endl;
    } else {
        cout << "无环" << endl;
    }
    
    // 测试用例5: []（空链表）
    ListNode* head5 = nullptr;
    
    cout << "\n测试用例5: []（空链表）" << endl;
    
    ListNode* result5 = solution.detectCycle(head5);
    cout << "方法1（快慢指针）结果: ";
    if (result5) {
        cout << "环的入口节点值: " << result5->val << endl;
    } else {
        cout << "无环" << endl;
    }
    
    // 释放内存
    freeList(head1, true);
    freeList(head2, true);
    freeList(head3, false);
    freeList(head4, true);
    
    return 0;
}

/*
 * 题目扩展：LeetCode 142. 环形链表II
 * 来源：LeetCode、LintCode、剑指Offer、牛客网
 * 
 * 题目描述：
 * 给定一个链表的头节点  head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。
 * 如果 pos 是 -1，则在该链表中没有环。注意：pos 不作为参数进行传递，仅仅是为了标识链表的实际情况。
 * 不允许修改链表。
 * 
 * 解题思路：
 * 1. Floyd的龟兔赛跑算法（快慢指针）：
 *    - 第一阶段：使用快慢指针找到相遇点
 *    - 第二阶段：将一个指针重置到链表头部，两个指针同速前进，相遇点即为环的入口
 * 2. 哈希表法：使用哈希表记录访问过的节点，第一次重复访问的节点即为环的入口
 * 3. 标记法：修改节点值或添加标志位（不推荐，会修改原链表）
 * 4. 暴力解法：对每个节点遍历后续节点，看是否回到自身
 * 
 * 时间复杂度：
 * - 快慢指针法：O(n)
 * - 哈希表法：O(n)
 * - 标记法：O(n)
 * - 暴力解法：O(n²)
 * 
 * 空间复杂度：
 * - 快慢指针法：O(1)
 * - 哈希表法：O(n)
 * - 标记法：O(1)，但会修改原链表
 * - 暴力解法：O(1)
 * 
 * 最优解：Floyd的龟兔赛跑算法（快慢指针），时间复杂度O(n)，空间复杂度O(1)
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 内存管理：对于有环链表，释放内存时需要特别处理，避免无限循环
 * 3. 代码可读性：快慢指针法虽然巧妙，但需要清晰的注释解释算法原理
 * 4. 性能优化：对于大型链表，避免使用暴力解法和会修改原链表的标记法
 * 
 * 与机器学习等领域的联系：
 * 1. 环检测算法在图算法和网络分析中有广泛应用
 * 2. 在分布式系统中，检测循环依赖可以使用类似的思想
 * 3. 在机器学习中，某些算法可能会陷入循环，需要检测机制
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用指针操作，注意处理有环链表的释放
 * Java: 自动内存管理，但需要注意内存泄漏问题
 * Python: 简洁的语法，但处理链表环时需要注意引用问题
 * 
 * 算法深度分析：
 * Floyd的龟兔赛跑算法是一种非常巧妙的解决方案。其正确性可以通过数学证明：当快慢指针在环中相遇时，快指针走过的距离是慢指针的两倍。设链表头到环入口的距离为a，环入口到相遇点的距离为b，相遇点再回到环入口的距离为c。则快指针走了a + b + k(b + c)，慢指针走了a + b，其中k是快指针在环中多走的圈数。由于快指针速度是慢指针的两倍，有a + b + k(b + c) = 2(a + b)，化简得a = c + (k - 1)(b + c)。这意味着，从链表头到环入口的距离等于从相遇点到环入口的距离加上环长度的整数倍。因此，将一个指针重置到链表头，另一个保持在相遇点，两者同速前进，必定在环入口相遇。
 */