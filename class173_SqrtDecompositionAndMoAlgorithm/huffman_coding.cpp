/**
 * Huffman编码实现 (C++简化版本)
 * 
 * Huffman编码是一种无损数据压缩算法，它根据字符出现频率构建最优二叉树，
 * 使得出现频率高的字符具有较短的编码，出现频率低的字符具有较长的编码。
 * 
 * 算法原理：
 * 1. 统计字符频率
 * 2. 构建Huffman树：每次选择频率最小的两个节点合并
 * 3. 生成编码表：从根节点到叶节点的路径即为字符的编码
 * 4. 编码和解码过程
 * 
 * 时间复杂度：
 * - 构建Huffman树：O(n log n)，其中n是不同字符的数量
 * - 编码/解码：O(m)，其中m是字符串长度
 * 
 * 空间复杂度：O(n)
 * 
 * 优势：
 * 1. 压缩率高，能够达到信息熵的理论极限
 * 2. 实现相对简单
 * 3. 适合处理具有明显统计特性的数据
 * 
 * 劣势：
 * 1. 需要两次扫描数据（一次统计频率，一次编码）
 * 2. 对于频率分布均匀的数据压缩效果不佳
 * 3. 编码和解码需要相同的Huffman树
 * 
 * 应用场景：
 * 1. 文件压缩（如ZIP格式）
 * 2. 图像压缩（如JPEG）
 * 3. 音频压缩
 */

// 定义最大字符数和相关常量
#define MAX_CHARS 256
#define MAX_NODES 512
#define NULL 0

/**
 * Huffman树节点
 * 
 * 使用数组索引方式表示树结构，避免指针操作
 * character: 节点表示的字符（仅叶节点有效，内部节点为'\0'）
 * frequency: 字符出现的频率（节点权重）
 * left: 左子树在节点数组中的索引（-1表示无左子树）
 * right: 右子树在节点数组中的索引（-1表示无右子树）
 */
typedef struct {
    char character;      // 字符（叶节点有效）
    int frequency;       // 频率
    int left;            // 左子树索引（-1表示无左子树）
    int right;           // 右子树索引（-1表示无右子树）
} Node;

/**
 * Huffman编码器
 * 
 * 使用结构体封装Huffman编码所需的所有数据结构
 * nodes: 节点数组，存储所有Huffman树节点
 * nodeCount: 当前节点数量
 * root: Huffman树根节点在数组中的索引（-1表示空树）
 * codes: 字符到编码的映射表，使用二维数组存储每个字符的编码
 * codeLengths: 每个字符编码的长度，用于提高编码效率
 */
typedef struct {
    Node nodes[MAX_NODES];     // 节点数组，存储所有Huffman树节点
    int nodeCount;             // 节点数量
    int root;                  // 根节点索引（-1表示空树）
    char codes[MAX_CHARS][MAX_CHARS]; // 字符到编码的映射表
    int codeLengths[MAX_CHARS];       // 每个字符编码的长度
} HuffmanCoding;

/**
 * 初始化Huffman编码器
 * 
 * 将Huffman编码器的所有成员变量初始化为默认值
 * nodeCount设置为0，表示还没有节点
 * root设置为-1，表示空树
 * codes数组清空，codeLengths数组置零
 * 
 * @param hc 指向Huffman编码器结构体的指针
 */
void initHuffmanCoding(HuffmanCoding* hc) {
    // 初始化节点计数器
    hc->nodeCount = 0;
    // 初始化根节点索引为-1（表示空树）
    hc->root = -1;
    // 初始化编码表和编码长度数组
    for (int i = 0; i < MAX_CHARS; i++) {
        // 将每个字符的编码设置为空字符串
        hc->codes[i][0] = '\0';
        // 将每个字符的编码长度设置为0
        hc->codeLengths[i] = 0;
    }
}

/**
 * 优先队列（最小堆）实现
 * 
 * 使用数组实现的最小堆，用于高效获取频率最小的节点索引
 * data: 存储节点索引的数组
 * size: 当前队列中的元素数量
 */
typedef struct {
    int data[MAX_NODES];  // 存储节点索引的数组
    int size;             // 当前队列中的元素数量
} PriorityQueue;

/**
 * 初始化优先队列
 * 
 * 将优先队列的大小设置为0，表示空队列
 * 
 * @param pq 指向优先队列结构体的指针
 */
void initPriorityQueue(PriorityQueue* pq) {
    pq->size = 0;
}

/**
 * 向优先队列添加元素
 * 
 * 将元素添加到队列末尾，然后通过上浮调整维护堆性质
 * 时间复杂度：O(log n)
 * 
 * @param pq 指向优先队列结构体的指针
 * @param value 要添加的元素（节点索引）
 */
void pushPriorityQueue(PriorityQueue* pq, int value) {
    // 检查队列是否已满
    if (pq->size >= MAX_NODES) return;
    
    // 将元素添加到队列末尾
    pq->data[pq->size] = value;
    pq->size++;
    
    // 上浮调整：维护最小堆性质
    int i = pq->size - 1;
    while (i > 0) {
        // 计算父节点索引
        int parent = (i - 1) / 2;
        // 如果父节点不大于当前节点，调整结束
        if (pq->data[parent] <= pq->data[i]) break;
        // 交换当前节点与父节点
        int temp = pq->data[parent];
        pq->data[parent] = pq->data[i];
        pq->data[i] = temp;
        // 继续向上调整
        i = parent;
    }
}

/**
 * 从优先队列取出最小元素
 * 
 * 取出堆顶元素（最小值），然后通过下沉调整维护堆性质
 * 时间复杂度：O(log n)
 * 
 * @param pq 指向优先队列结构体的指针
 * @return 堆顶元素（最小值），如果队列为空则返回-1
 */
int popPriorityQueue(PriorityQueue* pq) {
    // 检查队列是否为空
    if (pq->size <= 0) return -1;
    
    // 取出堆顶元素
    int result = pq->data[0];
    pq->size--;
    
    // 如果队列不为空，需要调整堆结构
    if (pq->size > 0) {
        // 将最后一个元素移到堆顶
        pq->data[0] = pq->data[pq->size];
        
        // 下沉调整：维护最小堆性质
        int i = 0;
        while (1) {
            // 计算左右子节点索引
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            
            // 找到当前节点及其子节点中的最小值
            if (left < pq->size && pq->data[left] < pq->data[smallest]) {
                smallest = left;
            }
            if (right < pq->size && pq->data[right] < pq->data[smallest]) {
                smallest = right;
            }
            
            // 如果当前节点已经是最小的，调整结束
            if (smallest == i) break;
            
            // 交换当前节点与最小节点
            int temp = pq->data[i];
            pq->data[i] = pq->data[smallest];
            pq->data[smallest] = temp;
            // 继续向下调整
            i = smallest;
        }
    }
    
    return result;
}

/**
 * 创建新节点（叶节点）
 * 
 * 创建一个新的叶节点，用于表示输入字符串中的字符
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param character 节点表示的字符
 * @param frequency 字符出现的频率
 * @return 新节点在数组中的索引，如果节点数组已满则返回-1
 */
int createNode(HuffmanCoding* hc, char character, int frequency) {
    // 检查节点数组是否已满
    if (hc->nodeCount >= MAX_NODES) return -1;
    
    // 获取新节点的索引
    int index = hc->nodeCount;
    // 初始化节点属性
    hc->nodes[index].character = character;
    hc->nodes[index].frequency = frequency;
    hc->nodes[index].left = -1;   // 叶节点无左子树
    hc->nodes[index].right = -1;  // 叶节点无右子树
    // 增加节点计数器
    hc->nodeCount++;
    return index;
}

/**
 * 创建内部节点
 * 
 * 创建一个新的内部节点，作为两个子节点的父节点
 * 频率为两个子节点频率之和
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param left 左子节点的索引
 * @param right 右子节点的索引
 * @return 新节点在数组中的索引，如果节点数组已满则返回-1
 */
int createInternalNode(HuffmanCoding* hc, int left, int right) {
    // 检查节点数组是否已满
    if (hc->nodeCount >= MAX_NODES) return -1;
    
    // 获取新节点的索引
    int index = hc->nodeCount;
    // 初始化内部节点属性
    hc->nodes[index].character = '\0';  // 内部节点无字符
    // 内部节点频率等于两个子节点频率之和
    hc->nodes[index].frequency = hc->nodes[left].frequency + hc->nodes[right].frequency;
    hc->nodes[index].left = left;   // 设置左子树
    hc->nodes[index].right = right; // 设置右子树
    // 增加节点计数器
    hc->nodeCount++;
    return index;
}

/**
 * 判断是否为叶节点
 * 
 * 叶节点是没有子树的节点，用于存储实际字符
 * 在数组表示的树结构中，左右子树索引都为-1表示叶节点
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param nodeIndex 要判断的节点索引
 * @return 非0值表示是叶节点，0表示不是叶节点
 */
int isLeaf(HuffmanCoding* hc, int nodeIndex) {
    // 当节点的左右子树索引都为-1时，表示叶节点
    return hc->nodes[nodeIndex].left == -1 && hc->nodes[nodeIndex].right == -1;
}

/**
 * 构建Huffman树
 * 
 * 使用贪心算法构建最优二叉树，使得带权路径长度最小
 * 算法步骤：
 * 1. 统计输入字符串中各字符的出现频率
 * 2. 将所有字符节点放入优先队列（最小堆）
 * 3. 重复以下操作直到队列中只剩一个节点：
 *    a. 取出频率最小的两个节点
 *    b. 创建新节点作为它们的父节点，频率为两子节点频率之和
 *    c. 将新节点放回优先队列
 * 4. 剩下的唯一节点即为Huffman树的根节点
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param input 输入字符串
 */
void buildHuffmanTree(HuffmanCoding* hc, const char* input) {
    // 统计字符频率：遍历字符串，使用数组记录每个字符的出现次数
    int frequency[MAX_CHARS] = {0};
    int inputLen = 0;
    
    // 遍历输入字符串统计字符频率
    while (input[inputLen] != '\0') {
        // 使用unsigned char避免负数索引问题
        frequency[(unsigned char)input[inputLen]]++;
        inputLen++;
    }
    
    // 创建优先队列（最小堆）：用于高效获取频率最小的节点
    PriorityQueue pq;
    initPriorityQueue(&pq);
    
    // 为每个出现过的字符创建节点并加入优先队列
    for (int i = 0; i < MAX_CHARS; i++) {
        if (frequency[i] > 0) {
            // 创建叶节点
            int nodeIndex = createNode(hc, (char)i, frequency[i]);
            // 检查节点创建是否成功
            if (nodeIndex != -1) {
                // 将节点索引加入优先队列
                pushPriorityQueue(&pq, nodeIndex);
            }
        }
    }
    
    // 特殊情况处理：当输入字符串只包含一种字符时
    // 为了避免编码歧义，需要构造一个高度为2的树
    if (pq.size == 1) {
        int nodeIndex = popPriorityQueue(&pq);
        // 构造一个根节点，其右子树为原节点
        int internalIndex = createInternalNode(hc, -1, nodeIndex);
        hc->root = internalIndex;
        return;
    }
    
    // 构建Huffman树：贪心算法的核心实现
    // 每次从优先队列中取出频率最小的两个节点，合并为新节点后再放回队列
    while (pq.size > 1) {
        // 取出频率最小的两个节点作为左右子树
        int left = popPriorityQueue(&pq);   // 较小频率的节点作为左子树
        int right = popPriorityQueue(&pq);  // 较大频率的节点作为右子树
        // 创建父节点，频率为两个子节点频率之和
        int parent = createInternalNode(hc, left, right);
        // 将新节点放回优先队列
        pushPriorityQueue(&pq, parent);
    }
    
    // 最后剩下的节点即为Huffman树的根节点
    if (pq.size > 0) {
        hc->root = popPriorityQueue(&pq);
    }
}

/**
 * 递归构建编码表
 * 
 * 使用深度优先遍历Huffman树，为每个叶节点生成对应的二进制编码
 * 编码规则：
 * - 向左子树移动时，在编码末尾添加'0'
 * - 向右子树移动时，在编码末尾添加'1'
 * - 到达叶节点时，将字符与编码的映射关系保存到编码表中
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param nodeIndex 当前遍历到的节点索引
 * @param code 从根节点到当前节点的路径编码
 * @param codeLen 当前编码的长度
 */
void buildCodeTableRecursive(HuffmanCoding* hc, int nodeIndex, char* code, int codeLen) {
    // 递归终止条件：节点索引无效
    if (nodeIndex == -1) return;
    
    // 叶节点处理：保存字符到编码的映射关系
    if (isLeaf(hc, nodeIndex)) {
        // 在编码末尾添加字符串结束符
        code[codeLen] = '\0';
        // 获取叶节点表示的字符
        hc->nodes[nodeIndex].character;
        unsigned char ch = (unsigned char)hc->nodes[nodeIndex].character;
        // 将编码复制到编码表中
        for (int i = 0; i < codeLen; i++) {
            hc->codes[ch][i] = code[i];
        }
        hc->codes[ch][codeLen] = '\0';
        // 保存编码长度，提高编码效率
        hc->codeLengths[ch] = codeLen;
        return;
    }
    
    // 递归处理左右子树
    // 向左子树移动时，在编码末尾添加'0'
    if (hc->nodes[nodeIndex].left != -1) {
        code[codeLen] = '0';
        buildCodeTableRecursive(hc, hc->nodes[nodeIndex].left, code, codeLen + 1);
    }
    
    // 向右子树移动时，在编码末尾添加'1'
    if (hc->nodes[nodeIndex].right != -1) {
        code[codeLen] = '1';
        buildCodeTableRecursive(hc, hc->nodes[nodeIndex].right, code, codeLen + 1);
    }
}

/**
 * 构建编码表
 * 
 * 通过遍历Huffman树为每个字符生成对应的二进制编码
 * 编码规则：从根节点到叶节点的路径，左子树为'0'，右子树为'1'
 * 
 * @param hc 指向Huffman编码器结构体的指针
 */
void buildCodeTable(HuffmanCoding* hc) {
    // 检查Huffman树是否为空
    if (hc->root == -1) return;
    
    // 创建临时编码缓冲区
    char code[MAX_CHARS];
    // 从根节点开始递归构建编码表，初始编码长度为0
    buildCodeTableRecursive(hc, hc->root, code, 0);
}

/**
 * Huffman编码
 * 
 * 将输入字符串转换为Huffman编码的二进制字符串
 * 时间复杂度：O(m)，其中m是输入字符串的长度
 * 空间复杂度：O(k)，其中k是编码后字符串的长度
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param input 待编码的输入字符串
 * @param output 编码后的二进制字符串（需要预先分配足够空间）
 */
void encode(HuffmanCoding* hc, const char* input, char* output) {
    // 初始化输出字符串为空
    output[0] = '\0';
    // 输出字符串的当前位置索引
    int outputIndex = 0;
    
    // 遍历输入字符串中的每个字符
    int i = 0;
    while (input[i] != '\0') {
        // 获取当前字符（使用unsigned char避免负数问题）
        unsigned char ch = (unsigned char)input[i];
        // 从预计算的编码长度数组中获取编码长度
        int len = hc->codeLengths[ch];
        
        // 将字符对应的编码复制到输出字符串中
        for (int j = 0; j < len; j++) {
            output[outputIndex++] = hc->codes[ch][j];
        }
        i++;
    }
    
    // 在输出字符串末尾添加结束符
    output[outputIndex] = '\0';
}

/**
 * Huffman解码
 * 
 * 将Huffman编码的二进制字符串转换回原始字符串
 * 解码过程：从根节点开始，根据编码中的每一位（0或1）在Huffman树中移动
 * 当到达叶节点时，输出对应的字符并重新从根节点开始
 * 时间复杂度：O(k)，其中k是编码字符串的长度
 * 空间复杂度：O(m)，其中m是解码后字符串的长度
 * 
 * @param hc 指向Huffman编码器结构体的指针
 * @param encoded Huffman编码的二进制字符串
 * @param output 解码后的原始字符串（需要预先分配足够空间）
 */
void decode(HuffmanCoding* hc, const char* encoded, char* output) {
    // 初始化输出字符串为空
    output[0] = '\0';
    // 输出字符串的当前位置索引
    int outputIndex = 0;
    // 当前在Huffman树中的位置，初始为根节点
    int currentNode = hc->root;
    
    // 遍历编码字符串中的每一位
    int i = 0;
    while (encoded[i] != '\0' && currentNode != -1) {
        // 根据当前位的值在Huffman树中移动
        if (encoded[i] == '0') {
            // 遇到'0'，向左子树移动
            currentNode = hc->nodes[currentNode].left;
        } else {
            // 遇到'1'，向右子树移动
            currentNode = hc->nodes[currentNode].right;
        }
        
        // 安全检查：确保当前节点有效
        if (currentNode == -1) {
            // 如果节点无效，可能是编码错误或树结构问题
            break;
        }
        
        // 到达叶节点：输出字符并重新从根节点开始
        if (isLeaf(hc, currentNode)) {
            output[outputIndex++] = hc->nodes[currentNode].character;
            currentNode = hc->root;
        }
        
        i++;
    }
    
    // 在输出字符串末尾添加结束符
    output[outputIndex] = '\0';
}

/**
 * 计算压缩率
 */
double calculateCompressionRatio(int original, int compressed) {
    if (original == 0) return 0;
    return (1.0 - (double) compressed / original) * 100;
}

// 由于环境限制，不包含main函数和输出语句
// 算法核心功能已实现，可被其他程序调用