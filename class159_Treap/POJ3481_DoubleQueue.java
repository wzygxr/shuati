package class151;

// POJ 3481 Double Queue
// 维护一个双端队列，支持以下操作：
// 1. 插入元素
// 2. 查询并删除最大值
// 3. 查询并删除最小值
// 测试链接 : http://poj.org/problem?id=3481

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class POJ3481_DoubleQueue {

    // 最大节点数
    public static int MAXN = 100001;

    // 整棵树的头节点编号（根节点）
    public static int head = 0;

    // 空间使用计数，记录当前已分配的节点数量
    public static int cnt = 0;

    // 节点的key值（客户ID）
    public static int[] key = new int[MAXN];

    // 节点的priority值（优先级）
    public static int[] priority = new int[MAXN];

    // 左孩子节点索引数组
    public static int[] left = new int[MAXN];

    // 右孩子节点索引数组
    public static int[] right = new int[MAXN];

    // 子树大小数组，记录以每个节点为根的子树中节点总数
    public static int[] size = new int[MAXN];

    // 节点随机优先级数组，用于维护Treap的堆性质
    public static double[] randomPriority = new double[MAXN];

    /**
     * 更新节点信息
     * 计算以节点i为根的子树大小
     * @param i 节点索引
     */
    public static void up(int i) {
        // 子树大小 = 左子树大小 + 右子树大小 + 1（当前节点）
        size[i] = size[left[i]] + size[right[i]] + 1;
    }

    /**
     * 左旋转
     * 当右子节点的优先级大于当前节点时执行
     * @param i 当前节点
     * @return 旋转后的新根节点
     */
    public static int leftRotate(int i) {
        // 获取右子节点作为新的根节点
        int r = right[i];
        // 将右子节点的左子树作为当前节点的右子树
        right[i] = left[r];
        // 将当前节点作为原右子节点的左子树
        left[r] = i;
        // 更新节点信息
        up(i);
        up(r);
        // 返回新的根节点
        return r;
    }

    /**
     * 右旋转
     * 当左子节点的优先级大于当前节点时执行
     * @param i 当前节点
     * @return 旋转后的新根节点
     */
    public static int rightRotate(int i) {
        // 获取左子节点作为新的根节点
        int l = left[i];
        // 将左子节点的右子树作为当前节点的左子树
        left[i] = right[l];
        // 将当前节点作为原左子节点的右子树
        right[l] = i;
        // 更新节点信息
        up(i);
        up(l);
        // 返回新的根节点
        return l;
    }

    /**
     * 添加节点的递归实现
     * @param i 当前节点索引
     * @param id 客户ID
     * @param pri 优先级
     * @return 插入后的新节点索引
     */
    public static int add(int i, int id, int pri) {
        // 如果当前节点为空，创建新节点
        if (i == 0) {
            // 分配新节点
            key[++cnt] = id;
            priority[cnt] = pri;
            size[cnt] = 1;
            // 生成随机优先级
            randomPriority[cnt] = Math.random();
            // 返回新节点索引
            return cnt;
        }
        // 按优先级插入，优先级小的在左子树，优先级大的在右子树
        if (priority[i] < pri) {
            right[i] = add(right[i], id, pri);
        } else if (priority[i] > pri) {
            left[i] = add(left[i], id, pri);
        } else {
            // 如果优先级相等，按客户ID插入
            if (key[i] < id) {
                right[i] = add(right[i], id, pri);
            } else {
                left[i] = add(left[i], id, pri);
            }
        }
        // 更新当前节点的子树大小信息
        up(i);
        // 检查是否需要旋转以维护堆性质
        // 如果左子节点优先级大于当前节点，执行右旋
        if (left[i] != 0 && randomPriority[left[i]] > randomPriority[i]) {
            return rightRotate(i);
        }
        // 如果右子节点优先级大于当前节点，执行左旋
        if (right[i] != 0 && randomPriority[right[i]] > randomPriority[i]) {
            return leftRotate(i);
        }
        // 不需要旋转，返回当前节点
        return i;
    }

    /**
     * 添加元素的公共接口
     * @param id 客户ID
     * @param pri 优先级
     */
    public static void add(int id, int pri) {
        head = add(head, id, pri);
    }

    /**
     * 查找并删除最小值节点
     * @param i 当前节点索引
     * @return 最小值节点的索引
     */
    public static int removeMinNode(int i) {
        // 如果左子树为空，说明当前节点就是最小值节点
        if (left[i] == 0) {
            return i;
        }
        // 否则递归查找左子树中的最小值节点
        return removeMinNode(left[i]);
    }

    /**
     * 删除最小值并返回其ID
     * @return 最小值节点的ID，如果树为空返回-1
     */
    public static int removeMin() {
        // 如果树为空，返回-1
        if (head == 0) return -1;
        // 找到最小值节点
        int minNode = removeMinNode(head);
        // 获取最小值节点的ID
        int result = key[minNode];
        // 删除该节点
        head = remove(head, priority[minNode]);
        return result;
    }

    /**
     * 查找并删除最大值节点
     * @param i 当前节点索引
     * @return 最大值节点的索引
     */
    public static int removeMaxNode(int i) {
        // 如果右子树为空，说明当前节点就是最大值节点
        if (right[i] == 0) {
            return i;
        }
        // 否则递归查找右子树中的最大值节点
        return removeMaxNode(right[i]);
    }

    /**
     * 删除最大值并返回其ID
     * @return 最大值节点的ID，如果树为空返回-1
     */
    public static int removeMax() {
        // 如果树为空，返回-1
        if (head == 0) return -1;
        // 找到最大值节点
        int maxNode = removeMaxNode(head);
        // 获取最大值节点的ID
        int result = key[maxNode];
        // 删除该节点
        head = remove(head, priority[maxNode]);
        return result;
    }

    /**
     * 查找最小值
     * @param i 当前节点索引
     * @return 最小值，如果树为空返回-1
     */
    public static int findMin(int i) {
        // 如果树为空，返回-1
        if (i == 0) return -1;
        // 如果左子树为空，当前节点就是最小值节点
        if (left[i] == 0) return key[i];
        // 否则递归查找左子树中的最小值
        return findMin(left[i]);
    }

    /**
     * 查找最大值
     * @param i 当前节点索引
     * @return 最大值，如果树为空返回-1
     */
    public static int findMax(int i) {
        // 如果树为空，返回-1
        if (i == 0) return -1;
        // 如果右子树为空，当前节点就是最大值节点
        if (right[i] == 0) return key[i];
        // 否则递归查找右子树中的最大值
        return findMax(right[i]);
    }

    /**
     * 删除指定优先级的节点
     * @param i 当前节点索引
     * @param pri 要删除节点的优先级
     * @return 删除后的新节点索引
     */
    public static int remove(int i, int pri) {
        // 如果当前节点为空，返回0
        if (i == 0) return 0;
        // 根据优先级查找要删除的节点
        if (priority[i] < pri) {
            right[i] = remove(right[i], pri);
        } else if (priority[i] > pri) {
            left[i] = remove(left[i], pri);
        } else {
            // 找到要删除的节点
            // 如果是叶子节点，直接删除
            if (left[i] == 0 && right[i] == 0) {
                return 0;
            } 
            // 如果只有右子树，用右子树替代当前节点
            else if (left[i] == 0) {
                return right[i];
            } 
            // 如果只有左子树，用左子树替代当前节点
            else if (right[i] == 0) {
                return left[i];
            } 
            // 如果左右子树都存在，根据随机优先级决定旋转方向
            else {
                // 如果左子节点随机优先级更高，执行右旋
                if (randomPriority[left[i]] > randomPriority[right[i]]) {
                    i = rightRotate(i);
                    right[i] = remove(right[i], pri);
                } 
                // 如果右子节点随机优先级更高，执行左旋
                else {
                    i = leftRotate(i);
                    left[i] = remove(left[i], pri);
                }
            }
        }
        // 更新节点信息
        up(i);
        // 返回当前节点
        return i;
    }

    /**
     * 删除指定优先级的元素的公共接口
     * @param pri 要删除的优先级
     */
    public static void remove(int pri) {
        head = remove(head, pri);
    }

    /**
     * 清空数据结构，重置所有数组
     */
    public static void clear() {
        Arrays.fill(key, 1, cnt + 1, 0);
        Arrays.fill(priority, 1, cnt + 1, 0);
        Arrays.fill(left, 1, cnt + 1, 0);
        Arrays.fill(right, 1, cnt + 1, 0);
        Arrays.fill(size, 1, cnt + 1, 0);
        Arrays.fill(randomPriority, 1, cnt + 1, 0);
        cnt = 0;
        head = 0;
    }

    /**
     * 主函数，处理输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int command;
        // 循环处理命令
        while (true) {
            in.nextToken();
            command = (int) in.nval;
            
            // 命令0：程序结束
            if (command == 0) {
                break;
            } 
            // 命令1：插入元素
            else if (command == 1) {
                in.nextToken();
                int id = (int) in.nval;  // 客户ID
                in.nextToken();
                int priority = (int) in.nval;  // 优先级
                add(id, priority);
            } 
            // 命令2：查询并删除最大值
            else if (command == 2) {
                int max = removeMax();
                if (max != -1) {
                    out.println(max);
                } else {
                    out.println(0);
                }
            } 
            // 命令3：查询并删除最小值
            else if (command == 3) {
                int min = removeMin();
                if (min != -1) {
                    out.println(min);
                } else {
                    out.println(0);
                }
            }
        }
        
        clear();
        out.flush();
        out.close();
        br.close();
    }
}