package com.example.myeventapp2.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myeventapp2.data.response.Event
import com.example.myeventapp2.databinding.ActivityDetailBinding
import com.example.myeventapp2.ui.EventAdapter
import com.example.myeventapp2.ui.finished.FinishedViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val event = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Event>(EventAdapter.EXTRA_EVENT, Event::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Event>(EventAdapter.EXTRA_EVENT)
        }

        binding.btnLink.setOnClickListener {
        val intentBrowser = Intent(Intent.ACTION_VIEW)
        intentBrowser.data = Uri.parse(event?.link)
            startActivity(intentBrowser)
        }

        supportActionBar?.title = event?.ownerName

        detailViewModel.isLoading.observe(this) {
            if (!it) {
                binding.progressBar.visibility = View.GONE
                val quoteLeft = (event?.quota ?: 0)-(event?.registrants ?: 0)
                binding.tvQuotaDetail.text = "${if (quoteLeft == 0) "SOLD OUT!" else "Quota peserta tersisa: " + quoteLeft}"

                binding.tvDescriptionDetail.text = fromHTML(event?.description ?: "")
                binding.tvEventNameDetail.text = event?.name
                binding.tvEventOwnerNameDetail.text = "👤 ${event?.ownerName + "  ||  📅 " +event?.beginTime}"
                Glide.with(this)
                    .load(event?.mediaCover)
                    .into(binding.ivEventMediaCoverDetail)
            }
            else {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    fun fromHTML(html: String): String {
        val imageRegex = Regex("<img[^>]*>")

        // Remove image tags while preserving their content
        val sanitizedHtml = imageRegex.replace(html) { matchResult ->
            val content = matchResult.value.substringAfter(">").substringBefore("</img>")
            content.replace("<", "&lt;").replace(">", "&gt;") // Escape remaining HTML tags
        }

        return android.text.Html.fromHtml(sanitizedHtml, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
        }
}