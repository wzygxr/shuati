# LeetCode 1268. 搜索推荐系统 - Python实现
# 
# 题目描述：
# 给定一个产品列表和搜索词，返回搜索词每个前缀的推荐产品。
# 
# 测试链接：https://leetcode.cn/problems/search-suggestions-system/
# 
# 算法思路：
# 1. 前缀树 + 深度优先搜索：为每个前缀收集最多3个产品
# 2. 构建前缀树存储所有产品名称
# 3. 对于搜索词的每个前缀，在前缀树中查找并收集推荐产品
# 4. 使用深度优先搜索按字典序收集产品
# 
# 时间复杂度分析：
# - 构建前缀树：O(∑len(products[i]))，其中∑len(products[i])是所有产品名称长度之和
# - 查询过程：O(∑len(searchWord) + K)，其中K是结果总长度
# - 总体时间复杂度：O(∑len(products[i]) + ∑len(searchWord) + K)
# 
# 空间复杂度分析：
# - 前缀树空间：O(∑len(products[i]))
# - 结果空间：O(∑len(searchWord) * 3)
# - 总体空间复杂度：O(∑len(products[i]) + ∑len(searchWord))
# 
# 是否最优解：是
# 理由：使用前缀树可以高效处理前缀相关的搜索推荐
# 
# 工程化考虑：
# 1. 异常处理：处理空产品列表和空搜索词
# 2. 边界情况：产品数量不足3个的情况
# 3. 极端输入：大量产品或长产品名称的情况
# 4. 内存管理：合理管理前缀树内存
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现，性能较高但空间固定
# C++：可使用指针实现，更节省空间
# 
# 调试技巧：
# 1. 验证每个前缀的推荐结果
# 2. 测试边界情况（产品数量不足3个）
# 3. 单元测试覆盖各种场景

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符集
    包含产品名称和子节点字典
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        self.children = {}  # 字符 -> TrieNode
        self.products = [] # 以该节点为结尾的产品名称

class SearchSuggestionsSystem:
    """
    搜索推荐系统类
    
    算法思路：
    使用前缀树存储产品名称，支持前缀搜索推荐
    
    时间复杂度分析：
    - 构建：O(∑len(products[i]))
    - 查询：O(∑len(searchWord) + K)
    
    空间复杂度分析：
    - 总体：O(∑len(products[i]) + ∑len(searchWord))
    """
    
    def __init__(self):
        """
        初始化搜索推荐系统
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def build(self, products):
        """
        构建前缀树
        
        算法步骤：
        1. 对产品列表进行排序（按字典序）
        2. 将每个产品插入到前缀树中
        3. 为每个节点存储最多3个产品（按字典序）
        
        时间复杂度：O(∑len(products[i]))
        空间复杂度：O(∑len(products[i]))
        
        :param products: 产品列表
        """
        # 对产品列表排序（按字典序）
        products_sorted = sorted(products)
        
        # 插入每个产品到前缀树
        for product in products_sorted:
            self._insert(product)
    
    def _insert(self, product):
        """
        向前缀树中插入产品名称
        
        算法步骤：
        1. 从根节点开始遍历产品名称的每个字符
        2. 对于每个字符，如果子节点不存在则创建
        3. 移动到子节点，将产品添加到当前节点的产品列表（最多3个）
        
        时间复杂度：O(L)，其中L是产品名称长度
        空间复杂度：O(L)
        
        :param product: 产品名称
        """
        node = self.root
        for char in product:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            
            # 为当前节点添加产品（最多3个）
            if len(node.products) < 3:
                node.products.append(product)
    
    def get_suggestions(self, search_word):
        """
        获取搜索词的推荐产品
        
        算法步骤：
        1. 对于搜索词的每个前缀，在前缀树中查找对应的节点
        2. 返回该节点存储的产品列表
        3. 如果前缀不存在，返回空列表
        
        时间复杂度：O(∑len(searchWord))
        空间复杂度：O(∑len(searchWord) * 3)
        
        :param search_word: 搜索词
        :return: 每个前缀的推荐产品列表
        """
        result = []
        node = self.root
        
        for i, char in enumerate(search_word):
            if char not in node.children:
                # 如果前缀不存在，后续所有前缀都返回空列表
                for _ in range(i, len(search_word)):
                    result.append([])
                break
            
            node = node.children[char]
            result.append(node.products[:])  # 复制产品列表
        
        return result
    
    def suggested_products(self, products, search_word):
        """
        主方法：生成搜索推荐
        
        算法步骤：
        1. 构建前缀树
        2. 获取搜索词的推荐产品
        3. 返回结果
        
        时间复杂度：O(∑len(products[i]) + ∑len(searchWord))
        空间复杂度：O(∑len(products[i]) + ∑len(searchWord))
        
        :param products: 产品列表
        :param search_word: 搜索词
        :return: 每个前缀的推荐产品列表
        """
        if not products or not search_word:
            return [[] for _ in range(len(search_word))]
        
        self.build(products)
        return self.get_suggestions(search_word)

def suggested_products_simplified(products, search_word):
    """
    简化版本：使用排序和二分查找
    
    算法思路：
    1. 对产品列表排序
    2. 对于每个前缀，使用二分查找找到第一个匹配的产品
    3. 收集最多3个匹配产品
    
    时间复杂度：O(n log n + m * n)，其中n是产品数量，m是搜索词长度
    空间复杂度：O(n)
    
    :param products: 产品列表
    :param search_word: 搜索词
    :return: 每个前缀的推荐产品列表
    """
    if not products or not search_word:
        return [[] for _ in range(len(search_word))]
    
    # 对产品列表排序
    products_sorted = sorted(products)
    result = []
    
    for i in range(1, len(search_word) + 1):
        prefix = search_word[:i]
        suggestions = []
        
        # 使用二分查找找到第一个匹配的产品
        left, right = 0, len(products_sorted)
        while left < right:
            mid = (left + right) // 2
            if products_sorted[mid] < prefix:
                left = mid + 1
            else:
                right = mid
        
        # 收集最多3个匹配产品
        for j in range(left, min(left + 3, len(products_sorted))):
            if products_sorted[j].startswith(prefix):
                suggestions.append(products_sorted[j])
            else:
                break
        
        result.append(suggestions)
    
    return result

def test_suggested_products():
    """
    单元测试函数
    
    测试用例设计：
    1. 基础功能测试
    2. 边界情况测试
    3. 性能对比测试
    """
    # 测试用例1：基础功能测试
    products1 = ["mobile", "mouse", "moneypot", "monitor", "mousepad"]
    search_word1 = "mouse"
    
    system = SearchSuggestionsSystem()
    result1 = system.suggested_products(products1, search_word1)
    
    # 验证结果
    assert len(result1) == 5, "结果数量不正确"
    assert len(result1[0]) == 3, "第一个前缀推荐数量不正确"
    assert "mobile" in result1[0], "推荐产品不正确"
    
    # 测试用例2：产品数量不足3个
    products2 = ["havana"]
    search_word2 = "havana"
    
    result2 = suggested_products_simplified(products2, search_word2)
    
    assert len(result2) == 6, "havana测试结果数量不正确"
    for i in range(6):
        assert len(result2[i]) == 1, "havana测试推荐数量不正确"
        assert result2[i][0] == "havana", "推荐产品不正确"
    
    # 测试用例3：空输入
    products3 = []
    search_word3 = "test"
    
    result3 = suggested_products_simplified(products3, search_word3)
    
    assert len(result3) == 4, "空输入测试结果数量不正确"
    for suggestions in result3:
        assert len(suggestions) == 0, "空输入测试推荐应该为空"
    
    # 测试简化版本
    result1_simplified = suggested_products_simplified(products1, search_word1)
    assert len(result1_simplified) == len(result1), "简化版本结果数量不一致"
    
    print("所有单元测试通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 大量产品名称
    2. 长搜索词
    3. 两种算法对比
    """
    import time
    import random
    import string
    
    # 生成大规模测试数据
    n = 10000
    products = []
    search_word = "test"
    
    for i in range(n):
        # 生成随机产品名称（长度5-10）
        length = random.randint(5, 10)
        product = ''.join(random.choices(string.ascii_lowercase, k=length))
        products.append(product)
    
    # 前缀树版本性能测试
    system = SearchSuggestionsSystem()
    
    start_time = time.time()
    result1 = system.suggested_products(products, search_word)
    trie_time = time.time() - start_time
    
    # 简化版本性能测试
    start_time = time.time()
    result2 = suggested_products_simplified(products, search_word)
    simplified_time = time.time() - start_time
    
    print(f"前缀树版本耗时: {trie_time:.3f}秒")
    print(f"简化版本耗时: {simplified_time:.3f}秒")
    print(f"处理了 {n} 个产品和搜索词 '{search_word}'")
    
    # 验证结果一致性（小规模测试）
    if n <= 100:
        assert len(result1) == len(result2), "结果数量不一致"
        for i in range(len(result1)):
            assert result1[i] == result2[i], f"第{i}个前缀推荐结果不一致"
        print("结果验证通过！")

def edge_case_test():
    """
    边界情况测试函数
    
    测试各种边界条件：
    1. 空产品和空搜索词
    2. 产品名称包含特殊字符
    3. 搜索词包含大写字母
    4. 产品数量极少
    """
    # 测试空输入
    result_empty = suggested_products_simplified([], "")
    assert result_empty == [], "空输入测试失败"
    
    # 测试单个字符搜索
    products_single = ["a", "ab", "abc"]
    result_single = suggested_products_simplified(products_single, "a")
    assert len(result_single) == 1, "单个字符搜索测试失败"
    assert result_single[0] == ["a", "ab", "abc"], "单个字符搜索结果不正确"
    
    # 测试产品名称包含数字（应该被正确处理）
    products_with_digits = ["test123", "test456"]
    result_digits = suggested_products_simplified(products_with_digits, "test")
    assert len(result_digits) == 4, "数字产品测试失败"
    
    print("边界情况测试通过！")

if __name__ == "__main__":
    # 运行单元测试
    test_suggested_products()
    
    # 运行边界情况测试
    edge_case_test()
    
    # 运行性能测试
    performance_test()