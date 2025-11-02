// 复制带随机指针的链表
// 测试链接 : https://leetcode.cn/problems/copy-list-with-random-pointer/

// 带随机指针的链表节点定义
class Node {
public:
    int val;        // 节点值
    Node* next;     // 指向下一个节点的指针
    Node* random;   // 指向链表中任意节点的随机指针
    
    // 构造函数
    Node(int _val) {
        val = _val;
        next = nullptr;
        random = nullptr;
    }
};

// 解决方案类
class Solution {
public:
    // 复制带随机指针的链表
    // 方法：三步法 - 插入复制节点、设置random指针、分离链表
    // 时间复杂度：O(n) - 需要遍历链表三次
    // 空间复杂度：O(1) - 不使用额外空间（不计算结果链表）
    // 参数：
    //   head - 原链表的头节点
    // 返回值：复制链表的头节点
    Node* copyRandomList(Node* head) {
        // 边界条件检查：如果链表为空，直接返回nullptr
        if (head == nullptr) {
            return nullptr;
        }
        
        // 当前节点指针
        Node* cur = head;
        // 下一个节点指针
        Node* next = nullptr;
        
        // 第一步：在原链表每个节点后插入复制节点
        // 1 -> 2 -> 3 -> ...
        // 变成 : 1 -> 1' -> 2 -> 2' -> 3 -> 3' -> ...
        while (cur != nullptr) {
            // 保存下一个节点
            next = cur->next;
            // 创建当前节点的复制节点并插入到当前节点后面
            cur->next = new Node(cur->val);
            // 连接复制节点和原链表的下一个节点
            cur->next->next = next;
            // 移动到原链表的下一个节点
            cur = next;
        }
        
        // 重置当前节点指针到头节点
        cur = head;
        // 复制节点指针
        Node* copy = nullptr;
        
        // 第二步：利用上面新老节点的结构关系，设置每一个新节点的random指针
        while (cur != nullptr) {
            // 保存原链表的下一个节点
            next = cur->next->next;
            // 获取当前节点的复制节点
            copy = cur->next;
            // 设置复制节点的random指针
            // 如果原节点的random不为空，则复制节点的random指向原节点random的下一个节点（即原节点random的复制节点）
            // 否则复制节点的random为空
            copy->random = cur->random != nullptr ? cur->random->next : nullptr;
            // 移动到原链表的下一个节点
            cur = next;
        }
        
        // 复制链表的头节点
        Node* ans = head->next;
        // 重置当前节点指针到头节点
        cur = head;
        
        // 第三步：新老链表分离，老链表重新连在一起，新链表重新连在一起
        while (cur != nullptr) {
            // 保存原链表的下一个节点
            next = cur->next->next;
            // 获取当前节点的复制节点
            copy = cur->next;
            // 恢复原链表的连接
            cur->next = next;
            // 设置复制链表的连接
            copy->next = next != nullptr ? next->next : nullptr;
            // 移动到原链表的下一个节点
            cur = next;
        }
        
        // 返回复制链表的头节点
        return ans;
    }
};

/*
 * 题目扩展：LeetCode 138. 复制带随机指针的链表
 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
 * 链接：https://leetcode.cn/problems/copy-list-with-random-pointer/
 * 
 * 题目描述：
 * 给你一个长度为 n 的链表，每个节点包含一个额外增加的随机指针 random ，
 * 该指针可以指向链表中的任何节点或空节点。
 * 构造这个链表的深拷贝。深拷贝应该正好由 n 个全新节点组成，
 * 其中每个新节点的值都设为其对应的原节点的值。
 * 新节点的 next 指针和 random 指针也都应指向复制链表中的新节点，
 * 并使原链表和复制链表中的相同节点指向相同的节点。
 * 
 * 解题思路：
 * 1. 在原链表每个节点后插入复制节点
 * 2. 设置复制节点的random指针
 * 3. 分离原链表和复制链表
 * 
 * 时间复杂度：O(n) - 需要遍历链表三次
 * 空间复杂度：O(1) - 不使用额外空间（不计算结果链表）
 * 是否最优解：是
 * 
 * 工程化考量：
 * 1. 边界情况处理：空链表
 * 2. 异常处理：random指针可能为空
 * 3. 内存管理：确保正确分配新节点
 *    注意：在实际项目中，需要确保正确释放内存以避免内存泄漏
 * 
 * 与机器学习等领域的联系：
 * 1. 图结构复制在图神经网络中有应用
 * 2. 复杂数据结构的深拷贝在模型保存/加载时重要
 * 
 * 语言特性差异：
 * Java: new关键字创建对象，垃圾回收管理内存
 * C++: 需要手动new/delete管理内存
 * Python: 使用Node()构造函数创建对象
 * 
 * 极端输入场景：
 * 1. 空链表
 * 2. 单节点链表
 * 3. random指针全部为空
 * 4. random指针形成循环
 * 
 * 测试用例示例：
 * // Node* head = new Node(7);
 * // head->next = new Node(13);
 * // head->next->next = new Node(11);
 * // head->next->next->next = new Node(10);
 * // head->next->next->next->next = new Node(1);
 * // 
 * // head->random = nullptr;
 * // head->next->random = head;
 * // head->next->next->random = head->next->next->next->next;
 * // head->next->next->next->random = head->next->next;
 * // head->next->next->next->next->random = head;
 * // 
 * // Solution solution;
 * // Node* result = solution.copyRandomList(head);
 */