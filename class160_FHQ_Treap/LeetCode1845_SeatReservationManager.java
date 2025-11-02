package class152;

// LeetCode 1845. 座位预约管理系统 - Java实现
// 使用FHQ-Treap（无旋Treap）解决LeetCode 1845题
// 题目链接: https://leetcode.cn/problems/seat-reservation-manager/
// 题目描述: 设计一个座位预约管理系统，支持以下操作：
// 1. reserve(): 预约一个最小编号的可用座位
// 2. unreserve(seatNumber): 取消预约指定的座位
// 
// 解题思路:
// 使用FHQ-Treap维护被取消预约的座位集合，同时使用currentMax变量优化座位分配
// 实现O(log k)的操作复杂度，其中k是当前可用（被取消预约）的座位数

import java.util.Random;

class SeatManager {
    // FHQ-Treap节点定义
    private static class Node {
        int key;        // 座位号
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
    private int totalSeats;  // 总座位数
    private int currentMax;  // 当前最大已分配座位号
    
    public SeatManager(int n) {
        root = null;
        random = new Random();
        totalSeats = n;
        currentMax = 0;
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
    
    // 获取第一个可用座位（最左边的节点）
    private int getFirstAvailableSeat(Node node) {
        if (node == null) {
            return -1; // 没有可用座位
        }
        // 一直向左走，找到最小值
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }
    
    // 预约座位
    public int reserve() {
        int seatNumber;
        
        // 如果有空位（被取消预约的座位），优先使用最小的那个
        if (root != null) {
            seatNumber = getFirstAvailableSeat(root);
            
            // 从可用座位集合中移除该座位
            Node[] split1 = split(root, seatNumber);
            Node[] split2 = split(split1[0], seatNumber - 1);
            root = merge(split2[0], split1[1]);
        } else {
            // 没有被取消的座位，分配新的座位
            if (currentMax < totalSeats) {
                seatNumber = ++currentMax;
            } else {
                throw new RuntimeException("No seats available"); // 所有座位都被预约
            }
        }
        
        return seatNumber;
    }
    
    // 取消预约，将座位放回可用集合
    public void unreserve(int seatNumber) {
        // 验证座位号的有效性
        if (seatNumber < 1 || seatNumber > totalSeats) {
            throw new IllegalArgumentException("Invalid seat number");
        }
        
        // 只有已经被预约的座位才能取消预约
        // 这里可以通过检查是否小于等于currentMax来简单判断
        if (seatNumber <= currentMax) {
            // 将座位添加到可用集合中
            Node[] splitRes = split(root, seatNumber);
            Node newNode = new Node(seatNumber, random.nextInt());
            root = merge(merge(splitRes[0], newNode), splitRes[1]);
        }
    }
}

/**
 * Your SeatManager object will be instantiated and called as such:
 * SeatManager obj = new SeatManager(n);
 * int param_1 = obj.reserve();
 * obj.unreserve(seatNumber);
 */

/**
 * 【时间复杂度分析】
 * - reserve(): O(log k)，其中k是当前可用（被取消预约）的座位数
 * - unreserve(): O(log k)
 * 
 * 【空间复杂度分析】
 * - O(k)，其中k是当前可用（被取消预约）的座位数
 * 
 * 【优化说明】
 * 1. 使用FHQ-Treap维护被取消预约的座位集合
 * 2. 使用currentMax变量跟踪最大已分配座位号，避免每次都需要在树中查找
 * 3. getFirstAvailableSeat方法使用非递归实现，避免栈溢出
 * 
 * 【测试用例】
 * 输入：
 * ["SeatManager", "reserve", "reserve", "unreserve", "reserve", "reserve", "reserve", "unreserve"]
 * [[5], [], [], [2], [], [], [], [5]]
 * 输出：
 * [null, 1, 2, null, 2, 3, 4, null]
 * 
 * 解释：
 * SeatManager seatManager = new SeatManager(5); // 初始化有5个座位
 * seatManager.reserve();    // 返回 1，分配座位 1
 * seatManager.reserve();    // 返回 2，分配座位 2
 * seatManager.unreserve(2); // 取消座位 2 的预约，它现在变为可用
 * seatManager.reserve();    // 返回 2，重新分配座位 2
 * seatManager.reserve();    // 返回 3，分配座位 3
 * seatManager.reserve();    // 返回 4，分配座位 4
 * seatManager.unreserve(5); // 取消座位 5 的预约（虽然它未被预约，但我们的实现会进行参数检查）
 */