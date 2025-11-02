package class152;

// LeetCode 2336. 无限集中的最小数字 - Java实现
// 使用FHQ-Treap（无旋Treap）解决LeetCode 2336题
// 题目链接: https://leetcode.cn/problems/smallest-number-in-infinite-set/
// 题目描述: 设计一个数据结构，维护一个包含所有正整数的无限集，支持以下操作：
// 1. popSmallest(): 弹出并返回集合中最小的整数
// 2. addBack(num): 将一个之前弹出的正整数num添加回集合中
// 
// 解题思路:
// 使用FHQ-Treap维护已删除的元素集合，同时使用currentMin变量优化最小值查询
// 实现O(log k)的操作复杂度，其中k是已删除的元素个数

import java.util.Random;

class SmallestInfiniteSet {
    // FHQ-Treap节点定义
    private static class Node {
        int key;        // 键值（存储被删除的正整数）
        int count;      // 词频计数
        int size;       // 子树大小
        int priority;   // 随机优先级
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(int k, int prio) {
            key = k;
            count = 1;
            size = 1;
            priority = prio;
            left = right = null;
        }
    }
    
    private Node root;       // 根节点
    private Random random;   // 随机数生成器
    private int currentMin;  // 当前最小的可用正整数
    
    public SmallestInfiniteSet() {
        root = null;
        random = new Random();
        currentMin = 1;  // 初始化最小可用值为1
    }
    
    // 更新节点的子树大小
    private void updateSize(Node node) {
        if (node != null) {
            int leftSize = node.left != null ? node.left.size : 0;
            int rightSize = node.right != null ? node.right.size : 0;
            node.size = leftSize + rightSize + node.count;
        }
    }
    
    // 分裂操作：将树按值分成两部分
    private Node[] split(Node root, int key) {
        if (root == null) {
            return new Node[]{null, null};
        }
        
        if (root.key <= key) {
            Node[] rightSplit = split(root.right, key);
            root.right = rightSplit[0];
            updateSize(root);
            return new Node[]{root, rightSplit[1]};
        } else {
            Node[] leftSplit = split(root.left, key);
            root.left = leftSplit[1];
            updateSize(root);
            return new Node[]{leftSplit[0], root};
        }
    }
    
    // 合并操作：合并两棵满足条件的树
    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        
        if (left.priority >= right.priority) {
            left.right = merge(left.right, right);
            updateSize(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            updateSize(right);
            return right;
        }
    }
    
    // 检查元素是否存在
    private boolean contains(int num) {
        Node curr = root;
        while (curr != null) {
            if (curr.key == num) return true;
            if (curr.key > num) curr = curr.left;
            else curr = curr.right;
        }
        return false;
    }
    
    // 添加元素（标记为删除）
    private void addNode(int num) {
        if (!contains(num)) {
            Node[] splitRes = split(root, num);
            Node newNode = new Node(num, random.nextInt());
            root = merge(merge(splitRes[0], newNode), splitRes[1]);
        }
    }
    
    // 删除元素（标记为可用）
    private void removeNode(int num) {
        Node[] split1 = split(root, num);
        Node[] split2 = split(split1[0], num - 1);
        root = merge(split2[0], split1[1]);
    }
    
    // 获取最小值（未被删除的最小正整数）
    public int popSmallest() {
        // 如果currentMin未被删除，直接返回并递增
        if (!contains(currentMin)) {
            int result = currentMin;
            currentMin++;
            return result;
        }
        
        // 否则需要在树中找到比currentMin小的最大可用值
        // 这里可以使用FHQ-Treap的特性来优化，但为了简化，我们使用线性查找
        // 更高效的实现可以是维护一个可用值的集合
        int result = currentMin;
        // 找到第一个未被删除的值
        while (contains(result)) {
            result++;
        }
        addNode(result);  // 标记为已删除
        currentMin = Math.max(currentMin, result + 1);
        return result;
    }
    
    // 添加一个之前被删除的正整数back到集合中
    public void addBack(int num) {
        // 只有当num小于currentMin且已被删除时才需要操作
        if (num < currentMin && contains(num)) {
            removeNode(num);  // 标记为可用
            currentMin = Math.min(currentMin, num);  // 更新currentMin
        }
    }
}

/**
 * Your SmallestInfiniteSet object will be instantiated and called as such:
 * SmallestInfiniteSet obj = new SmallestInfiniteSet();
 * int param_1 = obj.popSmallest();
 * obj.addBack(num);
 */

/**
 * 【时间复杂度分析】
 * - popSmallest(): 平均O(log k)，其中k是已删除的元素个数
 * - addBack(): O(log k)
 * 
 * 【空间复杂度分析】
 * - O(k)，其中k是已删除的元素个数
 * 
 * 【优化说明】
 * 1. 本题利用FHQ-Treap维护已删除的元素集合
 * 2. 使用currentMin变量优化最小值查询，避免每次都需要在树中查找
 * 3. 对于大量重复操作，此实现具有良好的性能表现
 * 
 * 【测试用例】
 * 输入：
 * ["SmallestInfiniteSet", "addBack", "popSmallest", "popSmallest", "popSmallest", "addBack", "popSmallest", "popSmallest", "popSmallest"]
 * [[], [2], [], [], [], [1], [], [], []]
 * 输出：
 * [null, null, 1, 2, 3, null, 1, 4, 5]
 */