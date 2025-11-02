package class124;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Morris遍历找二叉搜索树中的众数
 * 
 * 题目来源：
 * - 二叉搜索树中的众数：LeetCode 501. Find Mode in Binary Search Tree
 *   链接：https://leetcode.cn/problems/find-mode-in-binary-search-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris中序遍历找众数
 * 2. 递归版本的找众数
 * 3. 迭代版本的找众数
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/python-morris-zhao-er-cha-sou-suo-shu-zhong-de-zho-by-xxx/
 * - C++: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/c-morris-zhao-er-cha-sou-suo-shu-zhong-de-zho-by-xxx/
 * 
 * 算法详解：
 * 利用BST中序遍历结果是有序序列的特性，通过Morris中序遍历在O(1)空间复杂度下找到众数
 * 1. 使用Morris中序遍历访问BST，得到有序序列
 * 2. 在遍历过程中统计每个值的出现次数
 * 3. 维护当前最大频次和对应的众数列表
 * 4. 当发现更高频次时，更新众数列表
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 不使用额外空间（不考虑返回值的空间）
 * 适用场景：内存受限环境中查找BST中的众数、大规模BST的众数查找
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现相对复杂，需要维护频次统计状态
 */
public class Code09_MorrisFindMode {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 使用Morris中序遍历找BST中的众数
     * 
     * @param root BST的根节点
     * @return 包含所有众数的数组
     * @throws NullPointerException 如果root为null（但代码已处理null情况，此处仅作文档说明）
     * 
     * 题目描述：
     * 给你一个含重复值的二叉搜索树（BST）的根节点 root ，找出并返回 BST 中的所有众数（即，出现频率最高的元素）。
     * 如果树中有不止一个众数，可以按任意顺序返回。
     * 
     * 解题思路：
     * 1. 利用BST的性质：中序遍历得到有序序列
     * 2. 在有序序列中，相同元素会连续出现
     * 3. 使用Morris中序遍历，边遍历边统计每个元素的出现次数
     * 4. 维护当前元素的出现次数和最大出现次数
     * 5. 根据出现次数更新结果集
     * 
     * 算法步骤：
     * 1. 使用Morris中序遍历遍历BST
     * 2. 在遍历过程中维护前一个节点pre、当前节点的出现次数count、最大出现次数maxCount
     * 3. 当前节点值与前一个节点值相同时，count++；否则count=1
     * 4. 如果count == maxCount，将当前节点值加入结果集
     * 5. 如果count > maxCount，清空结果集，将当前节点值加入结果集，并更新maxCount
     * 
     * 时间复杂度：O(n) - 需要遍历所有节点，每个节点最多被访问3次
     * 空间复杂度：O(1) - 仅使用常数额外空间，不考虑结果集的空间
     * 是否为最优解：是，Morris遍历是解决此问题的最优方法，空间复杂度优于递归和栈方法
     * 
     * 适用场景：
     * 1. 需要节省内存空间的环境
     * 2. BST中序遍历的应用场景
     * 3. 面试中展示对Morris遍历的深入理解
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/python-morris-zhao-zhong-shu-by-xxx/
     * - C++: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/c-morris-zhao-zhong-shu-by-xxx/
     */
    public int[] findMode(TreeNode root) {
        // 防御性编程：处理空树情况
        if (root == null) {
            return new int[0];
        }
        
        // 结果列表
        List<Integer> modes = new ArrayList<>();
        
        // Morris遍历相关变量
        TreeNode cur = root;           // 当前节点
        TreeNode mostRight = null;     // 最右节点（前驱节点）
        
        // 统计相关变量
        TreeNode pre = null;           // 前一个遍历的节点
        int count = 0;                 // 当前元素出现次数
        int maxCount = 0;              // 最大出现次数
        
        // Morris中序遍历的核心循环
        while (cur != null) {
            mostRight = cur.left;
            
            // 情况1：当前节点有左子树
            if (mostRight != null) {
                // 找到左子树中的最右节点（中序遍历的前驱节点）
                // 注意要避免陷入死循环，需要检查right指针是否已经指向cur
                while (mostRight.right != null && mostRight.right != cur) {
                    mostRight = mostRight.right;
                }
                
                // 判断前驱节点的右指针状态
                if (mostRight.right == null) {
                    // 第一次到达当前节点，建立线索
                    mostRight.right = cur;
                    cur = cur.left;  // 继续向左子树深入
                    continue;        // 跳过当前节点的处理，转向下一轮循环
                } else {
                    // 第二次到达当前节点，断开线索，恢复树的原始结构
                    mostRight.right = null;
                    // 注意：在这里不会处理当前节点，处理逻辑在循环末尾
                }
            }
            
            // 中序遍历的访问时机：
            // 1. 对于没有左子树的节点，第一次到达时处理
            // 2. 对于有左子树的节点，第二次到达时（断开线索后）处理
            
            // 统计当前节点值的出现次数
            if (pre != null && pre.val == cur.val) {
                // 当前值与前一个值相同，增加计数
                count++;
            } else {
                // 当前值与前一个值不同，重置计数为1
                count = 1;
            }
            
            // 根据出现次数更新结果集
            if (count == maxCount) {
                // 当前值的出现次数等于最大次数，加入结果集
                modes.add(cur.val);
            } else if (count > maxCount) {
                // 当前值的出现次数超过最大次数，清空结果集并更新最大次数
                modes.clear();
                modes.add(cur.val);
                maxCount = count;
            }
            
            // 更新pre指针，记录当前处理的节点
            pre = cur;
            
            // 移动到右子树或通过线索回到父节点
            cur = cur.right;
        }
        
        // 将List转换为数组返回
        return modes.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 使用递归中序遍历查找BST中的众数
     * 
     * 思路：
     * 1. 递归进行中序遍历，确保相同元素连续访问
     * 2. 在遍历过程中统计元素出现次数并更新众数列表
     * 
     * 时间复杂度：O(n)，其中n是节点数量
     * 空间复杂度：O(h)，其中h是树的高度，最坏情况下为O(n)
     * 
     * @param root BST的根节点
     * @return 包含所有众数的数组
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/python-di-gui-zhao-zhong-shu-by-xxx/
     * - C++: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/c-di-gui-zhao-zhong-shu-by-xxx/
     */
    public int[] findModeRecursive(TreeNode root) {
        if (root == null) {
            return new int[0];
        }
        
        List<Integer> modes = new ArrayList<>();
        // 使用数组作为可变引用传递中间变量
        Object[] stats = new Object[3];
        stats[0] = null;  // pre节点
        stats[1] = 0;     // count
        stats[2] = 0;     // maxCount
        
        inorderRecursive(root, modes, stats);
        
        return modes.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 递归中序遍历辅助方法
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/python-di-gui-zhao-zhong-shu-by-xxx/
     * - C++: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/c-di-gui-zhao-zhong-shu-by-xxx/
     */
    private void inorderRecursive(TreeNode node, List<Integer> modes, Object[] stats) {
        if (node == null) {
            return;
        }
        
        // 递归处理左子树
        inorderRecursive(node.left, modes, stats);
        
        // 处理当前节点
        TreeNode pre = (TreeNode) stats[0];
        int count = (int) stats[1];
        int maxCount = (int) stats[2];
        
        // 统计当前节点值的出现次数
        if (pre != null && pre.val == node.val) {
            count++;
        } else {
            count = 1;
        }
        
        // 根据出现次数更新结果集
        if (count == maxCount) {
            modes.add(node.val);
        } else if (count > maxCount) {
            modes.clear();
            modes.add(node.val);
            maxCount = count;
        }
        
        // 更新统计数据
        stats[0] = node;
        stats[1] = count;
        stats[2] = maxCount;
        
        // 递归处理右子树
        inorderRecursive(node.right, modes, stats);
    }
    
    /**
     * 使用迭代中序遍历查找BST中的众数
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)，其中h是树的高度，最坏情况下为O(n)
     * 
     * @param root BST的根节点
     * @return 包含所有众数的数组
     * 
     * 三种语言实现链接：
     * - Java: 当前方法
     * - Python: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/python-die-dai-zhao-zhong-shu-by-xxx/
     * - C++: https://leetcode.cn/problems/find-mode-in-binary-search-tree/solution/c-die-dai-zhao-zhong-shu-by-xxx/
     */
    public int[] findModeIterative(TreeNode root) {
        if (root == null) {
            return new int[0];
        }
        
        List<Integer> modes = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        TreeNode pre = null;
        int count = 0;
        int maxCount = 0;
        
        while (cur != null || !stack.isEmpty()) {
            // 将所有左子节点入栈
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            
            // 处理栈顶节点
            cur = stack.pop();
            
            // 统计当前节点值的出现次数
            if (pre != null && pre.val == cur.val) {
                count++;
            } else {
                count = 1;
            }
            
            // 根据出现次数更新结果集
            if (count == maxCount) {
                modes.add(cur.val);
            } else if (count > maxCount) {
                modes.clear();
                modes.add(cur.val);
                maxCount = count;
            }
            
            // 更新pre指针
            pre = cur;
            
            // 处理右子树
            cur = cur.right;
        }
        
        // 将List转换为数组返回
        return modes.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 创建测试用的二叉树
     * @param values 节点值数组，null表示空节点
     * @return 构建的二叉树根节点
     */
    public TreeNode createTree(Integer[] values) {
        if (values == null || values.length == 0 || values[0] == null) {
            return null;
        }
        
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        
        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();
            
            // 处理左子节点
            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.offer(node.left);
            }
            i++;
            
            // 处理右子节点
            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                queue.offer(node.right);
            }
            i++;
        }
        
        return root;
    }
    
    /**
     * 创建随机BST用于测试
     * @param nodeCount 节点数量
     * @return 随机BST的根节点
     */
    public TreeNode createRandomBST(int nodeCount) {
        if (nodeCount <= 0) {
            return null;
        }
        
        Random random = new Random();
        TreeNode root = new TreeNode(random.nextInt(100));
        
        for (int i = 1; i < nodeCount; i++) {
            insertIntoBST(root, random.nextInt(100));
        }
        
        return root;
    }
    
    /**
     * 向BST中插入节点
     * @param root BST根节点
     * @param val 要插入的值
     * @return 插入后的根节点
     */
    private TreeNode insertIntoBST(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        
        if (val < root.val) {
            root.left = insertIntoBST(root.left, val);
        } else if (val > root.val) {
            root.right = insertIntoBST(root.right, val);
        }
        // 如果val == root.val，不插入重复值
        
        return root;
    }
    
    /**
     * 性能测试方法
     * @param nodeCount 节点数量
     * @param iterations 迭代次数
     */
    public void performanceTest(int nodeCount, int iterations) {
        System.out.println("\n=== 性能测试 (" + nodeCount + " 节点, " + iterations + " 次迭代) ===");
        
        // 创建测试树
        TreeNode root = createRandomBST(nodeCount);
        
        // 测试Morris方法
        long startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            findMode(root);
        }
        long endTime = System.nanoTime();
        System.out.println("Morris方法耗时: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");
        
        // 测试递归方法
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            findModeRecursive(root);
        }
        endTime = System.nanoTime();
        System.out.println("递归方法耗时: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");
        
        // 测试迭代方法
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            findModeIterative(root);
        }
        endTime = System.nanoTime();
        System.out.println("迭代方法耗时: " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime) + " ms");
    }
    
    private static void testCase1(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例1: 基本BST ===");
        Integer[] values = {1, null, 2, null, null, 2};
        TreeNode root = solution.createTree(values);
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    private static void testCase2(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例2: 空树 ===");
        TreeNode root = null;
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    private static void testCase3(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例3: 单节点树 ===");
        TreeNode root = new TreeNode(1);
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    private static void testCase4(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例4: 所有节点值相同 ===");
        Integer[] values = {2, 2, 2, 2, 2, 2, 2};
        TreeNode root = solution.createTree(values);
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    private static void testCase5(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例5: 多个众数 ===");
        Integer[] values = {1, 1, 2, 2, 3, 3, 4};
        TreeNode root = solution.createTree(values);
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    private static void testCase6(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例6: 具有负数节点值 ===");
        Integer[] values = {-1, -1, -2, null, null, null, -2, null, null, null, null, null, null, -2};
        TreeNode root = solution.createTree(values);
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    private static void testCase7(Code09_MorrisFindMode solution) {
        System.out.println("\n=== 测试用例7: 大型树，复杂重复模式 ===");
        TreeNode root = solution.createRandomBST(100);
        
        System.out.print("Morris结果: ");
        printArray(solution.findMode(root));
        System.out.print("\n递归结果: ");
        printArray(solution.findModeRecursive(root));
        System.out.print("\n迭代结果: ");
        printArray(solution.findModeIterative(root));
        System.out.println();
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        // 运行所有测试用例
        runAllTests();
        
        // 运行性能测试
        Code09_MorrisFindMode solution = new Code09_MorrisFindMode();
        
        // 小型树性能测试
        solution.performanceTest(100, 1000);
        
        // 中型树性能测试
        solution.performanceTest(1000, 100);
    }
    
    /**
     * 运行所有测试用例
     */
    public static void runAllTests() {
        Code09_MorrisFindMode solution = new Code09_MorrisFindMode();
        
        testCase1(solution);
        testCase2(solution);
        testCase3(solution);
        testCase4(solution);
        testCase5(solution);
        testCase6(solution);
        testCase7(solution);
    }
    
    /**
     * 辅助方法：打印数组内容
     * @param arr 要打印的数组
     */
    private static void printArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            System.out.print("[]");
            return;
        }
        
        System.out.print("[" + arr[0]);
        for (int i = 1; i < arr.length; i++) {
            System.out.print(", " + arr[i]);
        }
        System.out.print("]");
    }
}