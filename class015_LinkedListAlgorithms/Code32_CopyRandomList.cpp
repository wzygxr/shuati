// 复制复杂链表 - 剑指Offer
// 测试链接: https://leetcode.cn/problems/copy-list-with-random-pointer/
#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

// 定义复杂链表节点结构
struct Node {
    int val;
    Node* next;
    Node* random;
    Node(int x) : val(x), next(nullptr), random(nullptr) {}
};

class Solution {
public:
    // 方法1: 使用哈希表存储原节点和新节点的映射关系
    Node* copyRandomList(Node* head) {
        if (!head) return nullptr;
        
        // 创建哈希表，键为原节点，值为对应的新节点
        unordered_map<Node*, Node*> nodeMap;
        
        // 第一次遍历：创建所有节点并建立映射关系
        Node* curr = head;
        while (curr) {
            nodeMap[curr] = new Node(curr->val);
            curr = curr->next;
        }
        
        // 第二次遍历：设置next和random指针
        curr = head;
        while (curr) {
            if (curr->next) {
                nodeMap[curr]->next = nodeMap[curr->next];
            }
            if (curr->random) {
                nodeMap[curr]->random = nodeMap[curr->random];
            }
            curr = curr->next;
        }
        
        // 返回新链表的头节点
        return nodeMap[head];
    }
    
    // 方法2: 原地复制，不使用额外空间（除了结果链表）
    Node* copyRandomListInPlace(Node* head) {
        if (!head) return nullptr;
        
        // 第一步：在每个原节点后插入对应的新节点
        Node* curr = head;
        while (curr) {
            Node* newNode = new Node(curr->val);
            newNode->next = curr->next;
            curr->next = newNode;
            curr = newNode->next;
        }
        
        // 第二步：设置新节点的random指针
        curr = head;
        while (curr) {
            if (curr->random) {
                curr->next->random = curr->random->next;
            }
            curr = curr->next->next; // 跳过新节点，移动到下一个原节点
        }
        
        // 第三步：拆分链表，将原链表和新链表分开
        Node* newHead = head->next;
        Node* newCurr = newHead;
        curr = head;
        
        while (curr) {
            curr->next = curr->next->next; // 恢复原链表的next指针
            if (newCurr->next) {
                newCurr->next = newCurr->next->next; // 设置新链表的next指针
            }
            curr = curr->next;
            newCurr = newCurr->next;
        }
        
        return newHead;
    }
    
    // 方法3: 递归解法
    Node* copyRandomListRecursive(Node* head) {
        unordered_map<Node*, Node*> visited; // 用于记录已复制的节点，避免循环引用导致无限递归
        return copyNode(head, visited);
    }
    
private:
    Node* copyNode(Node* node, unordered_map<Node*, Node*>& visited) {
        if (!node) return nullptr;
        
        // 如果当前节点已经复制过，直接返回
        if (visited.count(node)) {
            return visited[node];
        }
        
        // 创建新节点
        Node* newNode = new Node(node->val);
        visited[node] = newNode; // 记录到已访问映射中
        
        // 递归复制next和random指针
        newNode->next = copyNode(node->next, visited);
        newNode->random = copyNode(node->random, visited);
        
        return newNode;
    }
    
public:
    // 辅助方法：释放链表内存
    void freeList(Node* head) {
        while (head) {
            Node* temp = head;
            head = head->next;
            delete temp;
        }
    }
};

// 辅助函数：构建复杂链表
// nodes: 节点值数组
// randomPairs: 每对表示节点索引和其random指针指向的索引
Node* buildRandomList(vector<int>& nodes, vector<pair<int, int>>& randomPairs) {
    int n = nodes.size();
    if (n == 0) return nullptr;
    
    // 创建所有节点
    vector<Node*> nodeList(n);
    for (int i = 0; i < n; i++) {
        nodeList[i] = new Node(nodes[i]);
    }
    
    // 连接next指针
    for (int i = 0; i < n - 1; i++) {
        nodeList[i]->next = nodeList[i + 1];
    }
    
    // 设置random指针
    for (auto& pair : randomPairs) {
        int from = pair.first;
        int to = pair.second;
        if (to != -1) { // -1表示random为nullptr
            nodeList[from]->random = nodeList[to];
        }
    }
    
    return nodeList[0];
}

// 辅助函数：打印链表（用于验证）
void printList(Node* head) {
    while (head) {
        cout << "节点值: " << head->val;
        if (head->random) {
            cout << ", Random指向: " << head->random->val;
        } else {
            cout << ", Random指向: nullptr";
        }
        cout << endl;
        head = head->next;
    }
}

// 辅助函数：验证复制的链表是否正确
bool validateCopy(Node* original, Node* copy) {
    unordered_map<Node*, Node*> nodeMap;
    Node* origCurr = original;
    Node* copyCurr = copy;
    
    // 检查节点值和next指针，同时建立映射关系
    while (origCurr && copyCurr) {
        if (origCurr->val != copyCurr->val) {
            return false;
        }
        nodeMap[origCurr] = copyCurr;
        origCurr = origCurr->next;
        copyCurr = copyCurr->next;
    }
    
    // 确保两个链表长度相同
    if (origCurr || copyCurr) {
        return false;
    }
    
    // 检查random指针
    origCurr = original;
    copyCurr = copy;
    while (origCurr) {
        if ((origCurr->random == nullptr && copyCurr->random != nullptr) ||
            (origCurr->random != nullptr && copyCurr->random != nodeMap[origCurr->random])) {
            return false;
        }
        origCurr = origCurr->next;
        copyCurr = copyCurr->next;
    }
    
    return true;
}

// 主函数用于测试
int main() {
    Solution solution;
    
    // 测试用例1: [[7,null],[13,0],[11,4],[10,2],[1,0]]
    vector<int> nodes1 = {7, 13, 11, 10, 1};
    vector<pair<int, int>> randoms1 = {{0, -1}, {1, 0}, {2, 4}, {3, 2}, {4, 0}};
    Node* head1 = buildRandomList(nodes1, randoms1);
    
    cout << "测试用例1:\n原始链表:" << endl;
    printList(head1);
    
    Node* copy1 = solution.copyRandomList(head1);
    cout << "\n哈希表法复制结果:" << endl;
    printList(copy1);
    cout << "验证结果: " << (validateCopy(head1, copy1) ? "正确" : "错误") << endl;
    
    // 测试用例2: [[1,1],[2,1]]
    vector<int> nodes2 = {1, 2};
    vector<pair<int, int>> randoms2 = {{0, 1}, {1, 1}};
    Node* head2 = buildRandomList(nodes2, randoms2);
    
    cout << "\n测试用例2:\n原始链表:" << endl;
    printList(head2);
    
    Node* copy2 = solution.copyRandomListInPlace(head2);
    cout << "\n原地复制法复制结果:" << endl;
    printList(copy2);
    cout << "验证结果: " << (validateCopy(head2, copy2) ? "正确" : "错误") << endl;
    
    // 测试用例3: [[3,null],[3,0],[3,null]]
    vector<int> nodes3 = {3, 3, 3};
    vector<pair<int, int>> randoms3 = {{0, -1}, {1, 0}, {2, -1}};
    Node* head3 = buildRandomList(nodes3, randoms3);
    
    cout << "\n测试用例3:\n原始链表:" << endl;
    printList(head3);
    
    Node* copy3 = solution.copyRandomListRecursive(head3);
    cout << "\n递归法复制结果:" << endl;
    printList(copy3);
    cout << "验证结果: " << (validateCopy(head3, copy3) ? "正确" : "错误") << endl;
    
    // 测试用例4: 空链表
    Node* head4 = nullptr;
    Node* copy4 = solution.copyRandomList(head4);
    cout << "\n测试用例4:\n空链表复制结果: " << (copy4 == nullptr ? "正确" : "错误") << endl;
    
    // 测试用例5: 只有一个节点的链表
    vector<int> nodes5 = {1};
    vector<pair<int, int>> randoms5 = {{0, -1}};
    Node* head5 = buildRandomList(nodes5, randoms5);
    
    cout << "\n测试用例5:\n原始链表:" << endl;
    printList(head5);
    
    Node* copy5 = solution.copyRandomList(head5);
    cout << "\n复制结果:" << endl;
    printList(copy5);
    cout << "验证结果: " << (validateCopy(head5, copy5) ? "正确" : "错误") << endl;
    
    // 释放内存
    solution.freeList(head1);
    solution.freeList(copy1);
    solution.freeList(head2);
    solution.freeList(copy2);
    solution.freeList(head3);
    solution.freeList(copy3);
    solution.freeList(head5);
    solution.freeList(copy5);
    
    return 0;
}

/*
 * 题目扩展：复制复杂链表 - 剑指Offer
 * 来源：LeetCode、剑指Offer、牛客网
 * 
 * 题目描述：
 * 给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random，该指针可以指向链表中的任何节点或空节点。
 * 构造这个链表的 深拷贝。深拷贝应该正好由 n 个 全新 节点组成，其中每个新节点的值都设为其对应的原节点的值。
 * 新节点的 next 指针和 random 指针也都应指向复制链表中的新节点，并使原链表和复制链表中的这些指针能够表示相同的链表状态。
 * 复制链表中的指针都不应指向原链表中的节点。
 * 
 * 解题思路：
 * 1. 哈希表法：使用哈希表存储原节点和新节点的映射关系，分两次遍历完成复制
 * 2. 原地复制法：在原链表的每个节点后插入一个新节点，设置random指针后再拆分
 * 3. 递归法：递归复制每个节点，使用哈希表避免重复复制
 * 
 * 时间复杂度：
 * - 哈希表法：O(n)，需要两次遍历链表
 * - 原地复制法：O(n)，需要三次遍历链表
 * - 递归法：O(n)，每个节点只访问一次
 * 
 * 空间复杂度：
 * - 哈希表法：O(n)，需要哈希表存储映射关系
 * - 原地复制法：O(1)，不使用额外空间（除了结果链表）
 * - 递归法：O(n)，递归调用栈深度和哈希表大小
 * 
 * 最优解：原地复制法，空间复杂度更低
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表、单节点链表
 * 2. 内存管理：在C++中需要正确创建和释放节点内存
 * 3. 处理循环引用：确保random指针不会导致无限循环
 * 4. 代码可读性：不同方法各有优缺点，需要根据具体场景选择
 * 
 * 与机器学习等领域的联系：
 * 1. 深拷贝概念在对象序列化和分布式系统中非常重要
 * 2. 图数据结构的复制在神经网络和知识图谱中常见
 * 3. 哈希表映射技术在缓存和索引中有广泛应用
 * 
 * 语言特性差异：
 * C++: 需要手动管理内存，使用指针操作
 * Java: 提供深拷贝机制，但需要实现Cloneable接口
 * Python: 可以使用copy.deepcopy()进行深拷贝
 * 
 * 算法深度分析：
 * 本题的难点在于处理random指针，因为它可能指向链表中的任何节点或空节点。原地复制法是一种非常巧妙的解决方案，它通过在原链表中插入新节点，使得我们可以在O(1)时间内找到random指针应该指向的位置。
 */