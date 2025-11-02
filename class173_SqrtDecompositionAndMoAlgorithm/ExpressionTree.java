import java.util.*;

/**
 * 表达式树（Expression Tree）实现
 * 表达式树是一种用于表示数学表达式的二叉树
 * 其中内部节点表示运算符，叶节点表示操作数
 * 
 * 常见应用场景：
 * 1. 表达式计算与求值
 * 2. 表达式简化
 * 3. 表达式转换（中缀转后缀/前缀）
 * 4. 编译器和解释器中的语法树
 * 5. 数学表达式的可视化
 * 6. 布尔表达式的表示和求值
 * 7. 科学计算和计算器应用
 * 
 * 相关算法题目：
 * - LeetCode 150. 逆波兰表达式求值 https://leetcode.cn/problems/evaluate-reverse-polish-notation/
 * - LeetCode 224. 基本计算器 https://leetcode.cn/problems/basic-calculator/
 * - LeetCode 227. 基本计算器 II https://leetcode.cn/problems/basic-calculator-ii/
 * - LeetCode 772. 基本计算器 III https://leetcode.cn/problems/basic-calculator-iii/
 * - LintCode 366. 斐波纳契数列 https://www.lintcode.com/problem/366/
 * - 牛客 NC46 加起来和为目标值的组合 https://www.nowcoder.com/practice/75e6cd5b85ab41c6a7c43359a74e869a
 * - HackerRank Expression Evaluation https://www.hackerrank.com/challenges/expression-evaluation/problem
 * - CodeChef SNAKEEAT https://www.codechef.com/problems/SNAKEEAT
 * - USACO Section 3.4 The Primes https://usaco.org/index.php?page=viewproblem2&cpid=349
 * - AtCoder ABC182 E - Akari https://atcoder.jp/contests/abc182/tasks/abc182_e
 * - 杭电 OJ 1237 简单计算器 https://acm.hdu.edu.cn/showproblem.php?pid=1237
 * - SPOJ ONP - Transform the Expression https://www.spoj.com/problems/ONP/
 * - Codeforces 1077C - Good Array https://codeforces.com/problemset/problem/1077/C
 */

public class ExpressionTree {
    /**
     * 表达式树节点类
     */
    public static class TreeNode {
        String value;       // 节点值：运算符或操作数
        TreeNode left;      // 左子节点
        TreeNode right;     // 右子节点
        boolean isOperator; // 是否为运算符
        
        /**
         * 构造函数
         * @param value 节点值
         */
        public TreeNode(String value) {
            this.value = value;
            this.left = null;
            this.right = null;
            // 判断是否为运算符（+、-、*、/、^）
            this.isOperator = "+-*/^".contains(value);
        }
        
        /**
         * 判断是否为叶子节点（操作数）
         * @return 是否为叶子节点
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }
        
        @Override
        public String toString() {
            return value;
        }
    }
    
    private TreeNode root; // 表达式树的根节点
    
    /**
     * 构造函数
     */
    public ExpressionTree() {
        this.root = null;
    }
    
    /**
     * 从后缀表达式构建表达式树
     * @param postfix 后缀表达式
     */
    public void buildFromPostfix(String postfix) {
        // 分词处理
        String[] tokens = tokenizeExpression(postfix);
        Stack<TreeNode> stack = new Stack<>();
        
        for (String token : tokens) {
            TreeNode node = new TreeNode(token);
            
            if (node.isOperator) {
                // 运算符需要两个操作数，从栈中弹出
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("无效的后缀表达式：" + postfix);
                }
                
                // 注意：先弹出的是右操作数
                node.right = stack.pop();
                node.left = stack.pop();
            }
            // 操作数直接入栈
            stack.push(node);
        }
        
        // 最终栈中应该只有一个节点（根节点）
        if (stack.size() != 1) {
            throw new IllegalArgumentException("无效的后缀表达式：" + postfix);
        }
        
        root = stack.pop();
    }
    
    /**
     * 从前缀表达式构建表达式树
     * @param prefix 前缀表达式
     */
    public void buildFromPrefix(String prefix) {
        // 分词处理并反转顺序
        String[] tokens = tokenizeExpression(prefix);
        // 前缀表达式从右往左处理
        Stack<TreeNode> stack = new Stack<>();
        
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            TreeNode node = new TreeNode(token);
            
            if (node.isOperator) {
                // 运算符需要两个操作数，从栈中弹出
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("无效的前缀表达式：" + prefix);
                }
                
                // 注意：前缀表达式先弹出的是左操作数
                node.left = stack.pop();
                node.right = stack.pop();
            }
            // 操作数直接入栈
            stack.push(node);
        }
        
        // 最终栈中应该只有一个节点（根节点）
        if (stack.size() != 1) {
            throw new IllegalArgumentException("无效的前缀表达式：" + prefix);
        }
        
        root = stack.pop();
    }
    
    /**
     * 从中缀表达式构建表达式树
     * @param infix 中缀表达式
     */
    public void buildFromInfix(String infix) {
        // 中缀表达式转后缀表达式，再构建表达式树
        String postfix = infixToPostfix(infix);
        buildFromPostfix(postfix);
    }
    
    /**
     * 表达式分词处理
     * @param expression 表达式字符串
     * @return 分词后的数组
     */
    private String[] tokenizeExpression(String expression) {
        // 简单的分词处理，将表达式分割成操作数和运算符
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            // 跳过空白字符
            if (Character.isWhitespace(c)) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                continue;
            }
            
            // 处理运算符和括号
            if ("+-*/^()".indexOf(c) != -1) {
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                tokens.add(String.valueOf(c));
            } else {
                // 处理数字或变量
                currentToken.append(c);
            }
        }
        
        // 添加最后一个标记
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        
        return tokens.toArray(new String[0]);
    }
    
    /**
     * 获取运算符的优先级
     * @param operator 运算符
     * @return 优先级值（数字越大优先级越高）
     */
    private int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }
    
    /**
     * 中缀表达式转换为后缀表达式
     * @param infix 中缀表达式
     * @return 后缀表达式
     */
    private String infixToPostfix(String infix) {
        String[] tokens = tokenizeExpression(infix);
        StringBuilder postfix = new StringBuilder();
        Stack<String> stack = new Stack<>();
        
        for (String token : tokens) {
            // 操作数直接添加到结果
            if (!token.equals("(") && !token.equals(")") && !"+-*/^".contains(token)) {
                postfix.append(token).append(" ");
            }
            // 左括号入栈
            else if (token.equals("(")) {
                stack.push(token);
            }
            // 处理右括号
            else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop(); // 弹出左括号
                } else {
                    throw new IllegalArgumentException("括号不匹配的表达式：" + infix);
                }
            }
            // 处理运算符
            else {
                while (!stack.isEmpty() && !stack.peek().equals("(") && 
                       getPrecedence(stack.peek()) >= getPrecedence(token)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(token);
            }
        }
        
        // 将栈中剩余的运算符添加到结果
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                throw new IllegalArgumentException("括号不匹配的表达式：" + infix);
            }
            postfix.append(stack.pop()).append(" ");
        }
        
        return postfix.toString().trim();
    }
    
    /**
     * 计算表达式树的值
     * @return 计算结果
     */
    public double evaluate() {
        return evaluate(root);
    }
    
    /**
     * 递归计算表达式树的值
     * @param node 当前节点
     * @return 计算结果
     */
    private double evaluate(TreeNode node) {
        // 空节点，返回0
        if (node == null) {
            return 0;
        }
        
        // 叶节点是操作数，直接转换为数值
        if (node.isLeaf()) {
            try {
                return Double.parseDouble(node.value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("无法解析的操作数：" + node.value);
            }
        }
        
        // 递归计算左右子树的值
        double leftVal = evaluate(node.left);
        double rightVal = evaluate(node.right);
        
        // 根据运算符进行计算
        switch (node.value) {
            case "+":
                return leftVal + rightVal;
            case "-":
                return leftVal - rightVal;
            case "*":
                return leftVal * rightVal;
            case "/":
                if (Math.abs(rightVal) < 1e-10) {
                    throw new ArithmeticException("除零错误");
                }
                return leftVal / rightVal;
            case "^":
                return Math.pow(leftVal, rightVal);
            default:
                throw new IllegalArgumentException("未知的运算符：" + node.value);
        }
    }
    
    /**
     * 获取中缀表达式字符串（带括号）
     * @return 中缀表达式
     */
    public String toInfixNotation() {
        StringBuilder sb = new StringBuilder();
        toInfixNotation(root, sb);
        return sb.toString();
    }
    
    /**
     * 递归生成中缀表达式
     * @param node 当前节点
     * @param sb 结果字符串构建器
     */
    private void toInfixNotation(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        
        // 对于非叶节点（运算符），需要添加括号
        if (!node.isLeaf()) {
            sb.append("(");
        }
        
        // 递归处理左子树
        toInfixNotation(node.left, sb);
        
        // 添加当前节点值
        sb.append(node.value);
        
        // 递归处理右子树
        toInfixNotation(node.right, sb);
        
        // 对于非叶节点（运算符），需要添加右括号
        if (!node.isLeaf()) {
            sb.append(")");
        }
    }
    
    /**
     * 获取后缀表达式字符串
     * @return 后缀表达式
     */
    public String toPostfixNotation() {
        StringBuilder sb = new StringBuilder();
        toPostfixNotation(root, sb);
        return sb.toString().trim();
    }
    
    /**
     * 递归生成后缀表达式（后续遍历）
     * @param node 当前节点
     * @param sb 结果字符串构建器
     */
    private void toPostfixNotation(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        
        // 递归处理左右子树
        toPostfixNotation(node.left, sb);
        toPostfixNotation(node.right, sb);
        
        // 添加当前节点值
        sb.append(node.value).append(" ");
    }
    
    /**
     * 获取前缀表达式字符串
     * @return 前缀表达式
     */
    public String toPrefixNotation() {
        StringBuilder sb = new StringBuilder();
        toPrefixNotation(root, sb);
        return sb.toString().trim();
    }
    
    /**
     * 递归生成前缀表达式（前序遍历）
     * @param node 当前节点
     * @param sb 结果字符串构建器
     */
    private void toPrefixNotation(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        
        // 添加当前节点值
        sb.append(node.value).append(" ");
        
        // 递归处理左右子树
        toPrefixNotation(node.left, sb);
        toPrefixNotation(node.right, sb);
    }
    
    /**
     * 打印表达式树结构
     */
    public void printTree() {
        System.out.println("表达式树结构:");
        printTree(root, 0);
    }
    
    /**
     * 递归打印树结构
     * @param node 当前节点
     * @param level 当前节点深度
     */
    private void printTree(TreeNode node, int level) {
        if (node == null) {
            return;
        }
        
        // 先打印右子树（在上层）
        printTree(node.right, level + 1);
        
        // 打印当前节点
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(node.value);
        
        // 打印左子树（在下层）
        printTree(node.left, level + 1);
    }
    
    /**
     * 获取树的高度
     * @return 树的高度
     */
    public int getHeight() {
        return getHeight(root);
    }
    
    /**
     * 递归计算树的高度
     * @param node 当前节点
     * @return 以该节点为根的子树高度
     */
    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);
        
        return Math.max(leftHeight, rightHeight) + 1;
    }
    
    /**
     * 获取节点数量
     * @return 节点数量
     */
    public int getNodeCount() {
        return getNodeCount(root);
    }
    
    /**
     * 递归计算节点数量
     * @param node 当前节点
     * @return 以该节点为根的子树节点数量
     */
    private int getNodeCount(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        return getNodeCount(node.left) + getNodeCount(node.right) + 1;
    }
    
    /**
     * 获取叶节点数量（操作数数量）
     * @return 叶节点数量
     */
    public int getLeafCount() {
        return getLeafCount(root);
    }
    
    /**
     * 递归计算叶节点数量
     * @param node 当前节点
     * @return 以该节点为根的子树叶节点数量
     */
    private int getLeafCount(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        if (node.isLeaf()) {
            return 1;
        }
        
        return getLeafCount(node.left) + getLeafCount(node.right);
    }
    
    /**
     * 获取运算符节点数量
     * @return 运算符节点数量
     */
    public int getOperatorCount() {
        return getOperatorCount(root);
    }
    
    /**
     * 递归计算运算符节点数量
     * @param node 当前节点
     * @return 以该节点为根的子树运算符节点数量
     */
    private int getOperatorCount(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        int count = node.isOperator ? 1 : 0;
        return count + getOperatorCount(node.left) + getOperatorCount(node.right);
    }
    
    /**
     * 前序遍历表达式树
     * @return 前序遍历结果列表
     */
    public List<String> preorderTraversal() {
        List<String> result = new ArrayList<>();
        preorderTraversal(root, result);
        return result;
    }
    
    /**
     * 递归进行前序遍历
     * @param node 当前节点
     * @param result 结果列表
     */
    private void preorderTraversal(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        
        result.add(node.value);
        preorderTraversal(node.left, result);
        preorderTraversal(node.right, result);
    }
    
    /**
     * 中序遍历表达式树
     * @return 中序遍历结果列表
     */
    public List<String> inorderTraversal() {
        List<String> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }
    
    /**
     * 递归进行中序遍历
     * @param node 当前节点
     * @param result 结果列表
     */
    private void inorderTraversal(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        
        inorderTraversal(node.left, result);
        result.add(node.value);
        inorderTraversal(node.right, result);
    }
    
    /**
     * 后序遍历表达式树
     * @return 后序遍历结果列表
     */
    public List<String> postorderTraversal() {
        List<String> result = new ArrayList<>();
        postorderTraversal(root, result);
        return result;
    }
    
    /**
     * 递归进行后序遍历
     * @param node 当前节点
     * @param result 结果列表
     */
    private void postorderTraversal(TreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        
        postorderTraversal(node.left, result);
        postorderTraversal(node.right, result);
        result.add(node.value);
    }
    
    /**
     * 层序遍历表达式树
     * @return 层序遍历结果列表
     */
    public List<List<String>> levelOrderTraversal() {
        List<List<String>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<String> currentLevel = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.value);
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            result.add(currentLevel);
        }
        
        return result;
    }
    
    /**
     * 复制表达式树
     * @return 复制的表达式树
     */
    public ExpressionTree copy() {
        ExpressionTree newTree = new ExpressionTree();
        newTree.root = copyNode(root);
        return newTree;
    }
    
    /**
     * 递归复制节点
     * @param node 要复制的节点
     * @return 复制的节点
     */
    private TreeNode copyNode(TreeNode node) {
        if (node == null) {
            return null;
        }
        
        TreeNode newNode = new TreeNode(node.value);
        newNode.left = copyNode(node.left);
        newNode.right = copyNode(node.right);
        return newNode;
    }
    
    /**
     * 判断两个表达式树是否相等
     * @param other 另一个表达式树
     * @return 是否相等
     */
    public boolean equals(ExpressionTree other) {
        if (other == null) {
            return false;
        }
        return equalsNode(root, other.root);
    }
    
    /**
     * 递归判断两个节点是否相等
     * @param node1 第一个节点
     * @param node2 第二个节点
     * @return 是否相等
     */
    private boolean equalsNode(TreeNode node1, TreeNode node2) {
        if (node1 == null && node2 == null) {
            return true;
        }
        if (node1 == null || node2 == null) {
            return false;
        }
        
        return node1.value.equals(node2.value) && 
               equalsNode(node1.left, node2.left) && 
               equalsNode(node1.right, node2.right);
    }
    
    /**
     * 测试主函数
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        try {
            // 创建表达式树实例
            ExpressionTree tree = new ExpressionTree();
            
            // 测试从中缀表达式构建
            System.out.println("===== 从中缀表达式构建 =====");
            String infixExpression = "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3";
            tree.buildFromInfix(infixExpression);
            
            // 打印树结构
            tree.printTree();
            
            // 显示不同形式的表达式
            System.out.println("中缀表达式: " + tree.toInfixNotation());
            System.out.println("后缀表达式: " + tree.toPostfixNotation());
            System.out.println("前缀表达式: " + tree.toPrefixNotation());
            
            // 计算表达式值
            System.out.println("表达式值: " + tree.evaluate());
            
            // 显示树的统计信息
            System.out.println("树高: " + tree.getHeight());
            System.out.println("节点数: " + tree.getNodeCount());
            System.out.println("叶节点数(操作数): " + tree.getLeafCount());
            System.out.println("运算符节点数: " + tree.getOperatorCount());
            
            // 遍历结果
            System.out.println("前序遍历: " + String.join(" ", tree.preorderTraversal()));
            System.out.println("中序遍历: " + String.join(" ", tree.inorderTraversal()));
            System.out.println("后序遍历: " + String.join(" ", tree.postorderTraversal()));
            
            // 层序遍历
            System.out.println("层序遍历:");
            List<List<String>> levelOrder = tree.levelOrderTraversal();
            for (int i = 0; i < levelOrder.size(); i++) {
                System.out.println("层 " + (i + 1) + ": " + String.join(" ", levelOrder.get(i)));
            }
            
            // 测试从后缀表达式构建
            System.out.println("\n===== 从后缀表达式构建 =====");
            ExpressionTree tree2 = new ExpressionTree();
            String postfixExpression = "3 4 2 * 1 5 - 2 3 ^ ^ / +";
            tree2.buildFromPostfix(postfixExpression);
            System.out.println("构建的表达式值: " + tree2.evaluate());
            System.out.println("两棵树是否相等: " + tree.equals(tree2));
            
            // 测试从前缀表达式构建
            System.out.println("\n===== 从前缀表达式构建 =====");
            ExpressionTree tree3 = new ExpressionTree();
            String prefixExpression = "+ 3 / * 4 2 ^ ^ - 1 5 2 3";
            tree3.buildFromPrefix(prefixExpression);
            System.out.println("构建的表达式值: " + tree3.evaluate());
            System.out.println("与原始树是否相等: " + tree.equals(tree3));
            
            // 测试表达式复制
            ExpressionTree copyTree = tree.copy();
            System.out.println("\n===== 表达式树复制 =====");
            System.out.println("复制树的表达式值: " + copyTree.evaluate());
            System.out.println("与原始树是否相等: " + tree.equals(copyTree));
            
        } catch (Exception e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}