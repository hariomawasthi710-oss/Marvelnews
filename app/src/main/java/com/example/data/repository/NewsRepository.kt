package com.example.data.repository

import com.example.data.db.PublishedArticleDao
import com.example.data.db.PublishedArticleEntity
import com.example.data.db.SavedArticleDao
import com.example.data.db.SavedArticleEntity
import com.example.data.model.NewsArticle
import com.example.data.model.NewsCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepository(
    private val savedArticleDao: SavedArticleDao,
    private val publishedArticleDao: PublishedArticleDao
) {
    private val articles = listOf(
        // HOME & BREAKING
        NewsArticle(
            id = "art-1",
            title = "Marvel Studios Unveils Phase 6 Lineup & Avengers: Secret Wars Sneak Peek",
            summary = "Marvel Studios announces landmark updates for Phase 6, teasing the return of legendary characters and groundbreaking multiverse crossovers.",
            content = listOf(
                "LOS ANGELES — Marvel Studios delivered a thunderous presentation today outlining the thrilling road ahead for the Marvel Cinematic Universe. Kevin Feige unveiled exclusive concept reels for the upcoming Avengers films, sending shockwaves through pop culture and cinematic fandom worldwide.",
                "The studio confirmed that major multiverse story arcs will culminate in an unprecedented cinematic spectacle, bringing together classic heroes and rising stars from across the Marvel pantheon. Practical visual effects and IMAX dual-format filming will define the new production standard.",
                "In addition to tentpole theatrical releases, Marvel announced a brand new episodic series spotlighting street-level heroes in New York City, promising grounded storytelling and pulse-pounding action choreography."
            ),
            bulletPoints = listOf(
                "Avengers: Secret Wars confirmed as the multi-year culmination of the Multiverse Saga.",
                "Surprise casting announcements hint at fan-favorite cameos and returns.",
                "Next-gen IMAX enhancements and revolutionary visual styling confirmed."
            ),
            category = NewsCategory.HOME,
            publishedTime = "15 mins ago",
            readTimeMinutes = 3,
            author = "Entertainment Desk",
            imageUrl = "https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=800&q=80",
            city = "Los Angeles",
            isBreaking = true,
            viewCount = "48.2K"
        ),

        // NEWS (Global & National)
        NewsArticle(
            id = "art-2",
            title = "Global Digital Accord: 90 Nations Sign Landmark Data Sovereignty & Consumer Protection Pact",
            summary = "World leaders and technology ministers ratify a comprehensive digital charter establishing universal safeguards for personal privacy and cybersecurity.",
            content = listOf(
                "GENEVA — Delegates representing over 90 sovereign nations concluded a historic three-day summit today by signing the Global Digital Rights Framework. The treaty establishes binding international benchmarks for personal data security, cross-border digital identity, and safeguards against cyber fraud.",
                "The accord mandates telecom and financial institutions to deploy real-time automated fraud detection and establishes transparent arbitration mechanisms for consumers affected by digital identity breaches.",
                "A dedicated global technology development fund was also launched to bring modern high-speed optical fiber and satellite broadband to underserved communities across developing regions."
            ),
            bulletPoints = listOf(
                "Stringent consumer privacy rules ratified across North America, Europe, and Asia.",
                "Cross-border cybersecurity cooperation network set to launch next quarter.",
                "Over $12 Billion allocated for universal digital connectivity initiatives."
            ),
            category = NewsCategory.NEWS,
            publishedTime = "40 mins ago",
            readTimeMinutes = 3,
            author = "World Affairs Bureau",
            imageUrl = "https://images.unsplash.com/photo-1541872703-74c5e44368f9?w=800&q=80",
            city = "Geneva",
            isBreaking = false,
            viewCount = "29.4K"
        ),

        // FINANCE 1
        NewsArticle(
            id = "art-3",
            title = "Global Markets Rally as Tech Giants & Clean Energy Drive S&P and Sensex to All-Time Highs",
            summary = "Robust corporate earnings, cooling inflationary pressure, and aggressive clean-energy investments trigger widespread buying across equity markets.",
            content = listOf(
                "NEW YORK & MUMBAI — Equities surged across major international bourses as benchmark indices touched uncharted territory. Strong quarterly earnings from semiconductor leaders and fintech disruptors catalyzed broad institutional buying.",
                "Analysts highlighted that steady retail investment inflows combined with favorable interest rate stabilization have renewed investor appetite for both growth equities and safe-yield sovereign bonds.",
                "Commodity markets mirrored the upbeat sentiment, with copper and battery metals stabilizing amidst booming electric vehicle and infrastructure manufacturing demand."
            ),
            bulletPoints = listOf(
                "Benchmark indices close at record highs following strong corporate earnings.",
                "Technology, renewable energy, and banking sectors lead market gains.",
                "Retail investor participation through systematic investment plans reaches historic volumes."
            ),
            category = NewsCategory.FINANCE,
            publishedTime = "1 hour ago",
            readTimeMinutes = 3,
            author = "Financial Insights Team",
            imageUrl = "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&q=80",
            city = "New York",
            isBreaking = true,
            viewCount = "56.3K"
        ),

        // FINANCE 2
        NewsArticle(
            id = "art-4",
            title = "Instant Cross-Border UPI & Digital Currency Payments Expand to 15 New Countries",
            summary = "Travelers and international merchants can now complete zero-conversion cross-border payments directly via QR codes on mobile smartphones.",
            content = listOf(
                "SINGAPORE — Central banks and payment network operators announced a sweeping expansion of instant real-time settlement rails. International tourists and business travelers can now scan local merchant QR codes and settle transactions immediately in their native currency without exorbitant forex exchange fees.",
                "The initiative eliminates standard 3% to 6% card conversion markups, providing transparent exchange rates and instantaneous receipts directly on mobile banking apps.",
                "Industry experts predict that frictionless cross-border retail payments will boost international trade and tourism revenues by billions over the next decade."
            ),
            bulletPoints = listOf(
                "Instant QR payment integrations now active across Europe, Southeast Asia, and the Middle East.",
                "Eliminates heavy currency exchange spreads for international travelers.",
                "Seamless mobile bank integration supported by top tier financial institutions."
            ),
            category = NewsCategory.FINANCE,
            publishedTime = "2 hours ago",
            readTimeMinutes = 2,
            author = "Fintech Watch",
            imageUrl = "https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=800&q=80",
            city = "Singapore",
            isBreaking = false,
            viewCount = "22.1K"
        ),

        // TECH 1
        NewsArticle(
            id = "art-5",
            title = "On-Device Multimodal AI Breakthrough: Real-Time Offline Translation and Neural Assistants",
            summary = "Next-generation mobile neural processing units (NPUs) now enable fluid, multilingual voice translation and generative analysis entirely offline.",
            content = listOf(
                "SAN FRANCISCO — Technology pioneers unveiled revolutionary on-device neural language models capable of performing simultaneous audio translation across 30 languages without sending an ounce of data to cloud servers.",
                "Operating at ultra-low power consumption, these compact models run directly on modern smartphones and smart wearables, delivering private, instant voice transcription, real-time closed captions, and intelligent document summaries.",
                "Developers emphasize that local on-device execution guarantees total user privacy while empowering travelers, healthcare providers, and emergency responders with seamless communication in remote areas."
            ),
            bulletPoints = listOf(
                "Zero cloud reliance enables lightning-fast responses with complete data confidentiality.",
                "Supports real-time bidirectional translation across major global languages.",
                "System updates rolling out to compatible smartphones over the coming weeks."
            ),
            category = NewsCategory.TECH,
            publishedTime = "2 hours ago",
            readTimeMinutes = 3,
            author = "Tech Horizons",
            imageUrl = "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&q=80",
            city = "San Francisco",
            isBreaking = false,
            viewCount = "41.9K"
        ),

        // TECH 2
        NewsArticle(
            id = "art-6",
            title = "Next-Gen Holographic Displays & Spatial Computing Transform Interactive Media",
            summary = "Light-field optical displays and gesture-tracking wearables allow users to interact with high-definition 3D graphics in mid-air.",
            content = listOf(
                "TOKYO — Engineers have demonstrated the world's first daylight-visible holographic display, bringing science fiction closer to reality than ever before. Resembling technology out of Stark Industries, the system generates full-color volumetric imagery without requiring bulky headgear.",
                "The technology leverages micro-laser arrays and precise acoustic beamforming to let users manipulate holographic objects with tangible tactile feedback.",
                "Medical training, architectural CAD design, and high-end gaming are slated as the primary adoption sectors over the next twelve months."
            ),
            bulletPoints = listOf(
                "Glasses-free 3D light-field projection visible from wide viewing angles.",
                "Acoustic feedback creates the sensation of touching virtual objects.",
                "Commercial development kits scheduled for release later this year."
            ),
            category = NewsCategory.TECH,
            publishedTime = "3 hours ago",
            readTimeMinutes = 2,
            author = "Future Systems Bureau",
            imageUrl = "https://images.unsplash.com/photo-1535223289827-42f1e9919769?w=800&q=80",
            city = "Tokyo",
            isBreaking = false,
            viewCount = "33.8K"
        ),

        // HEALTH 1
        NewsArticle(
            id = "art-7",
            title = "The 2-Minute Mindfulness Protocol: How Micro-Resets Drastically Reduce Stress and Fatigue",
            summary = "Clinical researchers find that deliberate two-minute breathing micro-breaks recalibrate the nervous system and sharpen cognitive focus throughout the workday.",
            content = listOf(
                "BOSTON — A landmark study published by cognitive neuroscientists reveals that short, two-minute interval mindfulness exercises can lower salivary cortisol levels by up to 24% and boost sustained attention during demanding cognitive tasks.",
                "Rather than requiring extensive forty-minute meditation sessions, the researchers found that consistent micro-pauses—consisting of box breathing or mindful sensory grounding—prevent neural fatigue and stave off afternoon brain fog.",
                "Doctors recommend pairing brief digital reading pauses with conscious upright posture and hydration checks to promote cardiovascular vitality in desk workers."
            ),
            bulletPoints = listOf(
                "Two-minute focused breathing cycles measurably reduce acute stress biomarkers.",
                "Regular micro-breaks enhance mental stamina and analytical decision-making.",
                "Easy to integrate into daily reading routines and office workflow."
            ),
            category = NewsCategory.HEALTH,
            publishedTime = "3 hours ago",
            readTimeMinutes = 2,
            author = "Dr. Elena Vance, Wellness Desk",
            imageUrl = "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800&q=80",
            city = "Boston",
            isBreaking = false,
            viewCount = "38.5K"
        ),

        // DIGITAL MARKETING 1
        NewsArticle(
            id = "art-8",
            title = "Digital Marketing 2026: 5 Proven Blueprints to Supercharge Organic Reach and Brand Loyalty",
            summary = "Leading growth strategists break down how modern storytelling, community-first channels, and algorithmic video dominate digital brand expansion.",
            content = listOf(
                "LONDON — The digital creator and performance marketing landscape is evolving faster than ever. As traditional banner advertisements face diminishing engagement, top direct-to-consumer brands are pivoting toward authentic creator partnerships and hyper-personalized video content.",
                "Experts emphasize that high-retention short-form video coupled with actionable educational value outperforms generic promotional broadcasts by a ratio of four to one.",
                "Automated conversational funnels that convert social followers into direct subscriber relationships are proving to be the most resilient revenue engines of the decade."
            ),
            bulletPoints = listOf(
                "Short-form educational storytelling delivers 4x higher audience retention.",
                "Zero-party data and direct subscriber channels protect against algorithmic volatility.",
                "Micro-influencer collaborations drive superior conversion rates over broad celebrity ads."
            ),
            category = NewsCategory.DIGITAL_MARKETING,
            publishedTime = "4 hours ago",
            readTimeMinutes = 3,
            author = "Marcus Cole, Marketing Strategist",
            imageUrl = "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800&q=80",
            city = "London",
            isBreaking = false,
            viewCount = "67.4K"
        ),

        // DIGITAL MARKETING 2
        NewsArticle(
            id = "art-9",
            title = "Conversational Commerce: How Instant Messaging Channels Are Revolutionizing Local Retail",
            summary = "Automated chat storefronts and verified instant messaging allow boutique merchants to process orders and queries in under 30 seconds.",
            content = listOf(
                "BERLIN — Retailers are experiencing unprecedented sales growth by embracing interactive chat channels. Consumers can now browse product catalogs, request size recommendations, and complete checkout directly inside their favorite messaging apps.",
                "With integrated instant UPI and card tokens, payment confirmation takes mere seconds without requiring users to navigate complex third-party web portals.",
                "Marketing analysts forecast that chat-driven social commerce will exceed $70 Billion internationally by the close of the calendar year."
            ),
            bulletPoints = listOf(
                "Frictionless in-chat checkout eliminates shopping cart abandonment.",
                "Automated 24/7 customer service handles queries with human-like accuracy.",
                "Direct personalized offers deliver up to 35% higher repeat purchase rates."
            ),
            category = NewsCategory.DIGITAL_MARKETING,
            publishedTime = "5 hours ago",
            readTimeMinutes = 2,
            author = "E-Commerce Dispatch",
            imageUrl = "https://images.unsplash.com/photo-1557804506-669a67965ba0?w=800&q=80",
            city = "Berlin",
            isBreaking = false,
            viewCount = "25.8K"
        ),

        // TRENDING 1
        NewsArticle(
            id = "art-10",
            title = "Surprise Comic-Con Marvel Teaser Shatters Records with 60 Million Views in 24 Hours",
            summary = "Fandom explodes across social media as legendary superhero teasers and viral behind-the-scenes clips take over the trending charts.",
            content = listOf(
                "SAN DIEGO — The internet reached a fever pitch following the release of an unannounced teaser trailer showcase at Comic-Con. Amassing over sixty million views across video platforms in a single day, the preview ignited passionate discussions across Reddit, X, and Instagram.",
                "Cinematographers praised the vibrant lighting and dynamic practical choreography, while fan communities dissected every frame for easter eggs and multiverse clues.",
                "Cast members took to their social channels to express gratitude for the overwhelming global reception, promising that the best surprises remain firmly under wraps."
            ),
            bulletPoints = listOf(
                "Teaser climbs to #1 worldwide trending across YouTube and social channels.",
                "Fans praise return to rich comic-accurate aesthetics and practical effects.",
                "Viral countdown campaigns ignite anticipation for the upcoming theatrical debut."
            ),
            category = NewsCategory.TRENDING,
            publishedTime = "1 hour ago",
            readTimeMinutes = 2,
            author = "Viral Trends Desk",
            imageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=800&q=80",
            city = "San Diego",
            isBreaking = true,
            viewCount = "95.6K"
        ),

        // TRENDING 2
        NewsArticle(
            id = "art-11",
            title = "World Championship Thriller: Last-Minute Victory Sparks Unprecedented Global Celebrations",
            summary = "Fans pour into city streets waving flags as an electrifying final play secures a historic world championship title.",
            content = listOf(
                "PARIS — In one of the most thrilling finishes in sporting history, the underdog squad clinched the championship trophy on the absolute final play of stoppage time. Stadium crowds erupted into deafening cheers before jubilant celebrations spilled into city plazas across the globe.",
                "Highlights of the game-winning maneuver garnered tens of millions of views within hours, earning praise from international sports legends and heads of state alike.",
                "The victorious team captain delivered an emotional post-match address that has become the most shared video clip of the week."
            ),
            bulletPoints = listOf(
                "Dramatic stoppage-time winner seals one of the closest tournament finals on record.",
                "Over 15 million celebratory posts surge to the top of social media trends.",
                "Trophy parade and civic honoring ceremony scheduled for this weekend."
            ),
            category = NewsCategory.TRENDING,
            publishedTime = "2 hours ago",
            readTimeMinutes = 2,
            author = "Sports Beat",
            imageUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=800&q=80",
            city = "Paris",
            isBreaking = false,
            viewCount = "118.2K"
        )
    )

    private fun PublishedArticleEntity.toNewsArticle(): NewsArticle {
        return NewsArticle(
            id = id,
            title = title,
            summary = summary,
            content = contentJoined.split("\n\n").filter { it.isNotBlank() }.ifEmpty { listOf(summary) },
            bulletPoints = bulletPointsJoined.split("\n").filter { it.isNotBlank() },
            category = NewsCategory.fromId(categoryId),
            publishedTime = publishedTime,
            readTimeMinutes = readTimeMinutes,
            author = author,
            imageUrl = imageUrl,
            city = city,
            isBreaking = isBreaking,
            viewCount = viewCount,
            isOwnerPost = true
        )
    }

    fun observeAllArticles(): Flow<List<NewsArticle>> {
        return publishedArticleDao.observePublishedArticles().map { publishedList ->
            val customArticles = publishedList.map { it.toNewsArticle() }
            customArticles + articles
        }
    }

    suspend fun publishArticle(
        title: String,
        summary: String,
        content: List<String>,
        bulletPoints: List<String>,
        category: NewsCategory,
        author: String,
        imageUrl: String,
        city: String?,
        isBreaking: Boolean,
        publisherEmail: String
    ): Result<NewsArticle> {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) {
            return Result.failure(Exception("Article title cannot be empty."))
        }
        val cleanSummary = summary.trim().ifEmpty { content.firstOrNull()?.take(120) ?: cleanTitle }
        val id = "published-${System.currentTimeMillis()}"
        val entity = PublishedArticleEntity(
            id = id,
            title = cleanTitle,
            summary = cleanSummary,
            contentJoined = content.filter { it.isNotBlank() }.joinToString("\n\n").ifEmpty { cleanSummary },
            bulletPointsJoined = bulletPoints.filter { it.isNotBlank() }.joinToString("\n"),
            categoryId = category.id,
            publishedTime = "Just now",
            readTimeMinutes = 2,
            author = author.trim().ifEmpty { "Hariom Awasthi (Owner)" },
            imageUrl = imageUrl.trim().ifEmpty { "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80" },
            city = city?.trim()?.ifEmpty { null } ?: "Global",
            isBreaking = isBreaking,
            viewCount = "1.1K",
            publisherEmail = publisherEmail,
            publishedTimestamp = System.currentTimeMillis()
        )
        publishedArticleDao.insertPublishedArticle(entity)
        return Result.success(entity.toNewsArticle())
    }

    suspend fun getAllArticlesAsync(): List<NewsArticle> {
        val published = publishedArticleDao.getAllPublishedArticles().map { it.toNewsArticle() }
        return published + articles
    }

    fun getAllArticles(): List<NewsArticle> = articles

    fun getArticlesByCategory(category: NewsCategory): List<NewsArticle> {
        return if (category == NewsCategory.HOME) {
            articles
        } else {
            articles.filter { it.category == category }
        }
    }

    fun getArticleById(id: String): NewsArticle? {
        return articles.firstOrNull { it.id == id }
    }

    fun searchArticles(query: String): List<NewsArticle> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return articles
        return articles.filter {
            it.title.lowercase().contains(q) ||
            it.summary.lowercase().contains(q) ||
            it.city?.lowercase()?.contains(q) == true ||
            it.author.lowercase().contains(q)
        }
    }

    fun getSavedArticleIds(): Flow<List<String>> = savedArticleDao.observeSavedArticleIds()

    suspend fun toggleBookmark(articleId: String) {
        val exists = savedArticleDao.isBookmarked(articleId)
        if (exists) {
            savedArticleDao.removeBookmark(articleId)
        } else {
            savedArticleDao.bookmarkArticle(SavedArticleEntity(articleId))
        }
    }
}
