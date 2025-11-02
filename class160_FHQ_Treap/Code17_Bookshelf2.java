package class152;

/**
 * 洛谷 P2596 [ZJOI2006]书架
 * 题目链接: https://www.luogu.com.cn/problem/P2596
 * 题目描述: 维护一个书架，支持以下操作：
 * 1. 将某本书置于顶部 (Top x)
 * 2. 将某本书置于底部 (Bottom x)
 * 3. 将某本书置于指定位置 (Insert x y)
 * 4. 询问某本书在书架上的位置 (Ask x)
 * 5. 从顶部取书 (Query Top)
 * 6. 从底部取书 (Query Bottom)
 */
public class Code17_Bookshelf2 {
    // FHQ-Treap节点结构
    static class Node {
        int key;        // 书的编号
        int priority;   // 随机优先级
        int size;       // 子树大小
        int position;   // 书在书架上的位置
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(int key) {
            this.key = key;
            this.priority = (int) (Math.random() * Integer.MAX_VALUE);
            this.size = 1;
            this.position = 0;
        }
    }
    
    private Node root;    // 根节点
    private int nodeCnt;  // 节点计数
    
    public Code17_Bookshelf2() {
        this.root = null;
        this.nodeCnt = 0;
    }
    
    // 更新节点信息
    private void update(Node node) {
        if (node != null) {
            node.size = (node.left != null ? node.left.size : 0) + 
                       (node.right != null ? node.right.size : 0) + 1;
        }
    }
    
    // 按位置分裂，将树按照位置pos分裂为两棵树
    private Node[] splitByPosition(Node root, int pos) {
        if (root == null) {
            return new Node[]{null, null};
        }
        
        int leftSize = (root.left != null ? root.left.size : 0);
        if (leftSize + 1 <= pos) {
            Node[] parts = splitByPosition(root.right, pos - leftSize - 1);
            root.right = parts[0];
            update(root);
            return new Node[]{root, parts[1]};
        } else {
            Node[] parts = splitByPosition(root.left, pos);
            root.left = parts[1];
            update(root);
            return new Node[]{parts[0], root};
        }
    }
    
    // 按书编号分裂，将树按照书编号bookId分裂为两棵树
    private Node[] splitByBookId(Node root, int bookId) {
        if (root == null) {
            return new Node[]{null, null};
        }
        
        if (root.key <= bookId) {
            Node[] parts = splitByBookId(root.right, bookId);
            root.right = parts[0];
            update(root);
            return new Node[]{root, parts[1]};
        } else {
            Node[] parts = splitByBookId(root.left, bookId);
            root.left = parts[1];
            update(root);
            return new Node[]{parts[0], root};
        }
    }
    
    // 合并操作，将两棵树合并为一棵树
    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        
        if (left.priority >= right.priority) {
            left.right = merge(left.right, right);
            update(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            update(right);
            return right;
        }
    }
    
    // 查找书的位置
    private int findPosition(Node root, int bookId) {
        if (root == null) {
            return -1;
        }
        if (root.key == bookId) {
            return root.position;
        } else if (root.key > bookId) {
            return findPosition(root.left, bookId);
        } else {
            return findPosition(root.right, bookId);
        }
    }
    
    // 根据位置查找书
    private int findBookByPosition(Node root, int pos) {
        if (root == null) {
            return -1;
        }
        
        int leftSize = (root.left != null ? root.left.size : 0);
        if (leftSize + 1 == pos) {
            return root.key;
        } else if (leftSize + 1 > pos) {
            return findBookByPosition(root.left, pos);
        } else {
            return findBookByPosition(root.right, pos - leftSize - 1);
        }
    }
    
    // 更新子树中所有书的位置
    private void updatePosition(Node node, int delta) {
        if (node == null) {
            return;
        }
        node.position += delta;
        updatePosition(node.left, delta);
        updatePosition(node.right, delta);
    }
    
    // 构建初始书架
    public void build(int n) {
        for (int i = 1; i <= n; i++) {
            Node newNode = new Node(i);
            newNode.position = i;
            root = merge(root, newNode);
        }
    }
    
    // 将书置于顶部
    public void top(int bookId) {
        // 查找书的位置
        int pos = findPosition(root, bookId);
        if (pos == -1 || pos == 1) {
            return; // 书不存在或已在顶部
        }
        
        // 分裂出前pos-1本书
        Node[] parts1 = splitByPosition(root, pos - 1);
        Node leftTree = parts1[0];
        Node rightTree = parts1[1];
        
        // 分裂出前pos本书
        Node[] parts2 = splitByPosition(rightTree, pos);
        Node middleTree = parts2[0];
        Node rightRightTree = parts2[1];
        
        // 取出要移动的书
        Node book = middleTree;
        book.position = 1;
        
        // 更新位置信息
        updatePosition(leftTree, 1); // 前面的书位置后移
        updatePosition(rightRightTree, -1); // 后面的书位置前移
        
        // 重新合并树
        root = merge(merge(book, leftTree), rightRightTree);
    }
    
    // 将书置于底部
    public void bottom(int bookId) {
        // 查找书的位置
        int pos = findPosition(root, bookId);
        if (pos == -1) {
            return; // 书不存在
        }
        
        int totalSize = (root != null ? root.size : 0);
        if (pos == totalSize) {
            return; // 书已在底部
        }
        
        // 分裂出前pos本书
        Node[] parts1 = splitByPosition(root, pos);
        Node leftTree = parts1[0];
        Node rightTree = parts1[1];
        
        // 分裂出前pos+1本书
        Node[] parts2 = splitByPosition(rightTree, 1);
        Node book = parts2[0];
        Node rightRightTree = parts2[1];
        
        // 更新位置信息
        book.position = totalSize;
        updatePosition(leftTree, 1); // 前面的书位置后移
        updatePosition(rightRightTree, -1); // 后面的书位置前移
        
        // 重新合并树
        root = merge(merge(leftTree, rightRightTree), book);
    }
    
    // 查询书的位置
    public int ask(int bookId) {
        return findPosition(root, bookId);
    }
    
    // 查询指定位置的书
    public int query(int pos) {
        return findBookByPosition(root, pos);
    }
    
    // 测试函数
    public static void main(String[] args) {
        Code17_Bookshelf2 bookshelf = new Code17_Bookshelf2();
        
        // 初始化书架，放入1到3本书
        bookshelf.build(3);
        
        // 示例操作
        bookshelf.top(2);  // 将书2移到顶部
        System.out.println("Book at position 1: " + bookshelf.query(1)); // 应该输出2
        System.out.println("Book at position 2: " + bookshelf.query(2)); // 应该输出1
        System.out.println("Book at position 3: " + bookshelf.query(3)); // 应该输出3
        
        bookshelf.bottom(1);  // 将书1移到底部
        System.out.println("Book at position 1: " + bookshelf.query(1)); // 应该输出2
        System.out.println("Book at position 2: " + bookshelf.query(2)); // 应该输出3
        System.out.println("Book at position 3: " + bookshelf.query(3)); // 应该输出1
        
        System.out.println("Position of book 2: " + bookshelf.ask(2)); // 应该输出1
        System.out.println("Position of book 3: " + bookshelf.ask(3)); // 应该输出2
        System.out.println("Position of book 1: " + bookshelf.ask(1)); // 应该输出3
    }
}