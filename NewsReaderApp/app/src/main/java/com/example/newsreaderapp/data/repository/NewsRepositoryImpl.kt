package com.example.newsreaderapp.data.repository

import com.example.newsreaderapp.data.local.ArticleDao
import com.example.newsreaderapp.data.local.ArticleEntity
import com.example.newsreaderapp.domain.Article
import com.example.newsreaderapp.domain.repository.NewsRepository
import com.example.newsreaderapp.domain.repository.Resource
import io.ktor.client.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(
    private val client: HttpClient,
    private val dao: ArticleDao
) : NewsRepository {

    // Kumpulan Berita Indonesia yang sudah diperbarui (Biru Muda / Tech / Lifestyle)
    private val beritaPool = listOf(
        ArticleEntity(1, "Eksplorasi Keindahan Pantai Tersembunyi di Lombok", "Nikmati jernihnya air laut dan pasir putih yang memanjakan mata di pesisir selatan.", "https://images.unsplash.com/photo-1512100356956-c158747abab1?q=80&w=600&auto=format&fit=crop", "https://www.indonesia.travel"),
        ArticleEntity(2, "Tips Hidup Sehat: Manfaat Minum Air Putih di Pagi Hari", "Simak bagaimana kebiasaan sederhana ini bisa mendetoks tubuh Anda secara alami.", "https://images.unsplash.com/photo-1548839140-29a749e1cf4d?q=80&w=600&auto=format&fit=crop", "https://www.alodokter.com"),
        ArticleEntity(3, "Inovasi Mobil Listrik Lokal Semakin Diminati Pasar", "Kendaraan ramah lingkungan kini menjadi pilihan utama kaum urban di kota besar.", "https://images.unsplash.com/photo-1593941707882-a5bba14938c7?q=80&w=600&auto=format&fit=crop", "https://www.otomotif.com"),
        ArticleEntity(4, "Resep Kopi Susu Gula Aren ala Kafe yang Bisa Dibuat di Rumah", "Tak perlu keluar rumah, nikmati sensasi kopi kekinian dengan bahan sederhana.", "https://images.unsplash.com/photo-1461023058943-07fcbe16d735?q=80&w=600&auto=format&fit=crop", "https://www.cookpad.com"),
        ArticleEntity(5, "Panduan Fotografi Smartphone: Ambil Foto Estetik Saat Liburan", "Maksimalkan kamera ponsel Anda dengan teknik komposisi dan pencahayaan yang tepat.", "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?q=80&w=600&auto=format&fit=crop", "https://www.digital-photography-school.com"),
        ArticleEntity(6, "Tren Mode 2024: Warna Pastel Kembali Mendominasi", "Koleksi busana musim ini mengedepankan kenyamanan dengan sentuhan warna lembut.", "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?q=80&w=600&auto=format&fit=crop", "https://www.vogue.com"),
        ArticleEntity(7, "Cara Jitu Mengatur Keuangan di Usia 20-an", "Mulailah investasi sejak dini untuk masa depan finansial yang lebih stabil.", "https://images.unsplash.com/photo-1554224155-6726b3ff858f?q=80&w=600&auto=format&fit=crop", "https://www.ojk.go.id"),
        ArticleEntity(8, "Mengenal Budaya Lokal Lewat Festival Seni Tahunan", "Ratusan seniman berkumpul merayakan keberagaman warisan nusantara.", "https://images.unsplash.com/photo-1533174072545-7a4b6ad7a6c3?q=80&w=600&auto=format&fit=crop", "https://www.budaya-indonesia.org")
    )

    override fun getArticles(): Flow<Resource<List<Article>>> = flow {
        emit(Resource.Loading())

        val localArticles = dao.getAllArticles().map { it.toArticle() }
        emit(Resource.Loading(data = localArticles))

        try {
            delay(1000)
            
            val randomArticles = beritaPool.shuffled().take(6)

            dao.clearArticles()
            dao.insertArticles(randomArticles)

            val updatedArticles = dao.getAllArticles().map { it.toArticle() }
            emit(Resource.Success(updatedArticles))

        } catch (e: Exception) {
            emit(Resource.Error(
                message = "Gagal update berita terbaru. Menampilkan berita offline.",
                data = localArticles
            ))
        }
    }

    override suspend fun getArticleById(id: Int): Article? {
        return dao.getArticleById(id)?.toArticle()
    }

    private fun ArticleEntity.toArticle() = Article(
        id = id,
        title = title,
        description = content,
        imageUrl = imageUrl,
        url = url
    )
}
