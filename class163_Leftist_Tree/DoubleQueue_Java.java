package class155;

import java.util.*;

/**
 * POJ 3481 Double Queue（双端队列）
 * 
 * 题目链接: http://poj.org/problem?id=3481
 * 
 * 题目描述：实现一个双端队列，支持以下操作：
 * 1. 插入一个客户，包含id和优先级
 * 2. 删除并返回优先级最高的客户
 * 3. 删除并返回优先级最低的客户
 * 
 * 解题思路：使用两个左偏树，一个维护最大值，一个维护最小值
 * 
 * 时间复杂度：所有操作均为O(log n)
 * 空间复杂度：O(n)
 */
public class DoubleQueue_Java {
    
    // 客户类
    static class Customer {
        int id;      // 客户ID
        int priority; // 客户优先级
        boolean deleted; // 标记是否被删除
        
        public Customer(int id, int priority) {
            this.id = id;
            this.priority = priority;
            this.deleted = false;
        }
    }
    
    // 左偏树节点类
    static class LeftistTreeNode {
        Customer customer;
        int dist;
        LeftistTreeNode left;
        LeftistTreeNode right;
        
        public LeftistTreeNode(Customer customer) {
            this.customer = customer;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    // 合并两个左偏树（用于最大堆）
    private LeftistTreeNode mergeMax(LeftistTreeNode a, LeftistTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        // 维护大根堆性质
        if (a.customer.priority < b.customer.priority) {
            LeftistTreeNode temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并右子树
        a.right = mergeMax(a.right, b);
        
        // 维护左偏性质
        if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
            LeftistTreeNode temp = a.left;
            a.left = a.right;
            a.right = temp;
        }
        
        // 更新距离
        a.dist = (a.right == null) ? 0 : a.right.dist + 1;
        return a;
    }
    
    // 合并两个左偏树（用于最小堆）
    private LeftistTreeNode mergeMin(LeftistTreeNode a, LeftistTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        // 维护小根堆性质
        if (a.customer.priority > b.customer.priority) {
            LeftistTreeNode temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并右子树
        a.right = mergeMin(a.right, b);
        
        // 维护左偏性质
        if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
            LeftistTreeNode temp = a.left;
            a.left = a.right;
            a.right = temp;
        }
        
        // 更新距离
        a.dist = (a.right == null) ? 0 : a.right.dist + 1;
        return a;
    }
    
    // 最大值堆的根节点
    private LeftistTreeNode maxHeapRoot;
    // 最小值堆的根节点
    private LeftistTreeNode minHeapRoot;
    // 存储所有客户，用于快速查找
    private Map<Integer, Customer> customers;
    
    public DoubleQueue_Java() {
        maxHeapRoot = null;
        minHeapRoot = null;
        customers = new HashMap<>();
    }
    
    // 插入一个客户
    public void insert(int id, int priority) {
        // 如果客户已存在，先删除旧记录
        if (customers.containsKey(id)) {
            delete(id);
        }
        
        // 创建新客户
        Customer customer = new Customer(id, priority);
        customers.put(id, customer);
        
        // 同时插入到最大堆和最小堆
        LeftistTreeNode node = new LeftistTreeNode(customer);
        maxHeapRoot = mergeMax(maxHeapRoot, node);
        minHeapRoot = mergeMin(minHeapRoot, node);
    }
    
    // 删除特定ID的客户（内部方法）
    private void delete(int id) {
        Customer customer = customers.get(id);
        if (customer != null) {
            customer.deleted = true;
            customers.remove(id);
        }
    }
    
    // 删除并返回优先级最高的客户
    public Customer deleteMax() {
        // 清理堆中已删除的节点
        while (maxHeapRoot != null && maxHeapRoot.customer.deleted) {
            maxHeapRoot = mergeMax(maxHeapRoot.left, maxHeapRoot.right);
        }
        
        if (maxHeapRoot == null) {
            return null; // 堆为空
        }
        
        // 获取最大值节点
        LeftistTreeNode maxNode = maxHeapRoot;
        Customer maxCustomer = maxNode.customer;
        
        // 从最大值堆中删除
        maxHeapRoot = mergeMax(maxHeapRoot.left, maxHeapRoot.right);
        
        // 标记客户为已删除
        maxCustomer.deleted = true;
        customers.remove(maxCustomer.id);
        
        return maxCustomer;
    }
    
    // 删除并返回优先级最低的客户
    public Customer deleteMin() {
        // 清理堆中已删除的节点
        while (minHeapRoot != null && minHeapRoot.customer.deleted) {
            minHeapRoot = mergeMin(minHeapRoot.left, minHeapRoot.right);
        }
        
        if (minHeapRoot == null) {
            return null; // 堆为空
        }
        
        // 获取最小值节点
        LeftistTreeNode minNode = minHeapRoot;
        Customer minCustomer = minNode.customer;
        
        // 从最小值堆中删除
        minHeapRoot = mergeMin(minHeapRoot.left, minHeapRoot.right);
        
        // 标记客户为已删除
        minCustomer.deleted = true;
        customers.remove(minCustomer.id);
        
        return minCustomer;
    }
    
    // 主函数，处理输入输出
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DoubleQueue_Java queue = new DoubleQueue_Java();
        
        while (true) {
            int command = scanner.nextInt();
            if (command == 0) {
                break; // 结束程序
            } else if (command == 1) {
                // 插入操作
                int id = scanner.nextInt();
                int priority = scanner.nextInt();
                queue.insert(id, priority);
            } else if (command == 2) {
                // 删除最大值
                Customer maxCust = queue.deleteMax();
                if (maxCust != null) {
                    System.out.println(maxCust.id);
                }
            } else if (command == 3) {
                // 删除最小值
                Customer minCust = queue.deleteMin();
                if (minCust != null) {
                    System.out.println(minCust.id);
                }
            }
        }
        
        scanner.close();
    }
}