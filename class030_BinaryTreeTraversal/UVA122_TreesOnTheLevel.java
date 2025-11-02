package class036;

import java.util.*;

// UVA 122. Trees on the Level
// 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=58
// 题目大意: 按照层序遍历的方式构建二叉树并输出节点值。输入格式为(value, path)，其中path是由L和R组成的字符串，
// 表示从根节点到该节点的路径，L表示左子节点，R表示右子节点。

public class UVA122_TreesOnTheLevel {
    
    // 二叉树节点定义
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }
    
    // 节点信息类，用于存储节点值和路径
    static class TreeNodeInfo {
        int val;
        String path;
        
        TreeNodeInfo(int val, String path) {
            this.val = val;
            this.path = path;
        }
    }
    
    /**
     * 构建二叉树并进行层序遍历
     * 思路:
     * 1. 解析输入的节点信息，按照路径构建二叉树
     * 2. 对构建的二叉树进行层序遍历
     * 3. 如果构建过程中发现节点重复或缺失，返回空列表
     * 时间复杂度: O(n) - n是节点数量
     * 空间复杂度: O(n) - 存储节点和队列
     */
    public static List<Integer> levelOrderTraversal(List<TreeNodeInfo> nodes) {
        // 创建根节点
        TreeNode root = new TreeNode(0); // 临时根节点
        
        // 根据路径信息构建树
        for (TreeNodeInfo nodeInfo : nodes) {
            if (!insertNode(root, nodeInfo.val, nodeInfo.path)) {
                // 如果插入失败，返回空列表
                return new ArrayList<>();
            }
        }
        
        // 进行层序遍历
        return bfs(root);
    }
    
    /**
     * 根据路径插入节点
     * @param root 根节点
     * @param val 节点值
     * @param path 路径字符串
     * @return 是否插入成功
     */
    private static boolean insertNode(TreeNode root, int val, String path) {
        TreeNode current = root;
        
        // 根据路径找到要插入的位置
        for (int i = 0; i < path.length(); i++) {
            char direction = path.charAt(i);
            if (direction == 'L') {
                if (current.left == null) {
                    current.left = new TreeNode(0); // 临时节点
                }
                current = current.left;
            } else if (direction == 'R') {
                if (current.right == null) {
                    current.right = new TreeNode(0); // 临时节点
                }
                current = current.right;
            } else {
                // 无效路径字符
                return false;
            }
        }
        
        // 检查节点是否已经被赋值
        if (current.val != 0) {
            // 节点已经被赋值，说明重复
            return false;
        }
        
        // 赋值
        current.val = val;
        return true;
    }
    
    /**
     * 层序遍历
     * @param root 根节点
     * @return 遍历结果
     */
    private static List<Integer> bfs(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            
            // 如果节点值为0，说明是临时节点，树不完整
            if (current.val == 0) {
                return new ArrayList<>(); // 返回空列表表示树不完整
            }
            
            result.add(current.val);
            
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例测试
        List<TreeNodeInfo> nodes = new ArrayList<>();
        nodes.add(new TreeNodeInfo(5, ""));
        nodes.add(new TreeNodeInfo(3, "L"));
        nodes.add(new TreeNodeInfo(4, "LL"));
        nodes.add(new TreeNodeInfo(7, "LR"));
        
        List<Integer> result = levelOrderTraversal(nodes);
        if (result.isEmpty()) {
            System.out.println("not complete");
        } else {
            for (int i = 0; i < result.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(result.get(i));
            }
            System.out.println();
        }
    }
}