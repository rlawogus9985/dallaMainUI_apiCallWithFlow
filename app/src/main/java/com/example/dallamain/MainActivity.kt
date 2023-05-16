package com.example.dallamain

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.dallamain.adapter.BannerAdapter
import com.example.dallamain.adapter.FollowingAdapter
import com.example.dallamain.adapter.LiveSectionAdapter
import com.example.dallamain.adapter.Top10Adapter
import com.example.dallamain.adapter.TopBnrAdapter
import com.example.dallamain.data.Top10Data
import com.example.dallamain.databinding.ActivityMainBinding
import com.example.dallamain.databinding.ItemTopbnrBinding
import com.example.dallamain.decorator.FollowingDecorator
import com.example.dallamain.decorator.Top10Decorator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), View.OnScrollChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var jobTopBnr: Job
    private lateinit var jobAdBnr: Job

    private var actionbarWChangeBoundary: Int = 0

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.scrollView.setOnScrollChangeListener(this)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this


        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.followingRecylcerView.addItemDecoration(FollowingDecorator())

        refreshSettings(binding.refresh)

        binding.footerLayout.itemIconTintList = null

        // BottomNavigationView 띄우기
        val navigationBarHeight = getNavigationBarHeight(this)
        val footerParams = binding.footerLayout.layoutParams as ViewGroup.MarginLayoutParams
        footerParams.setMargins(0,0,0,navigationBarHeight)
        binding.footerLayout.layoutParams = footerParams

        // actionBarW 높이설정
        val actionbarParams = binding.actionBarW.layoutParams as ViewGroup.MarginLayoutParams
        actionbarParams.height += getStatusBarHeight(this)
        binding.actionBarW.layoutParams = actionbarParams

        binding.actionBarW.setOnTouchListener { _, _ -> true }


        val top10List = arrayListOf(
            Top10Data("https://og-data.s3.amazonaws.com/media/artworks/half/A0880/A0880-0016.jpg", "가나다", R.drawable.number_w_1),
            Top10Data("https://img.lovepik.com/photo/40173/9974.jpg_wh860.jpg", "라마바사", R.drawable.number_w_2),
            Top10Data("https://img.freepik.com/free-photo/beautiful-landscape_1417-1582.jpg", "아자차", R.drawable.number_w_3),
            Top10Data("https://t1.daumcdn.net/cfile/tistory/99B192495BA481842D", "카타파하", R.drawable.number_w_4),
            Top10Data("https://newsimg-hams.hankookilbo.com/2022/04/08/fdc1edb7-1352-48b2-8b5d-1b8906c3edfa.jpg", "ABCDE", R.drawable.number_w_5),
            Top10Data("https://og-data.s3.amazonaws.com/media/artworks/half/A0880/A0880-0016.jpg", "가나다", R.drawable.number_w_6),
            Top10Data("https://img.lovepik.com/photo/40173/9974.jpg_wh860.jpg", "라마바사", R.drawable.number_w_7),
            Top10Data("https://img.freepik.com/free-photo/beautiful-landscape_1417-1582.jpg", "아자차", R.drawable.number_w_8),
            Top10Data("https://t1.daumcdn.net/cfile/tistory/99B192495BA481842D", "카타파하", R.drawable.number_w_9),
            Top10Data("https://newsimg-hams.hankookilbo.com/2022/04/08/fdc1edb7-1352-48b2-8b5d-1b8906c3edfa.jpg", "ABCDE", R.drawable.number_w_10)
        )

        binding.top10Recylcerview.apply {
            adapter = Top10Adapter(top10List)
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(Top10Decorator())
        }

        val spannable = SpannableString(binding.announceText.text)
        spannable.setSpan(StyleSpan(Typeface.BOLD), 5, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(Typeface.BOLD), 12, 20, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.announceText.text = spannable

        // Sticky ScrollView(Custom)
        binding.scrollView.apply {
            tabLayoutSection = binding.tabLayoutLiveSection
            actionBarW = binding.actionBarW
        }

        setObserve()
        mainViewModelFlow()
    } // onCreate()

    override fun onResume() {
        super.onResume()
        topBnrAutoScroll()
        adBnrAutoScroll()
    }
    // viewModel의 api다시 불러오기
    private fun getApiData() = with(mainViewModel){
        getTopBnrLists()
        getFollowingList()
        getEventList()
        getLiveDataList()
    }

    private fun refreshSettings(swipeRefreshLayout: SwipeRefreshLayout) = with(swipeRefreshLayout){
        setColorSchemeResources(R.color.white)
        setProgressBackgroundColorSchemeResource(R.color.pink)
        setOnRefreshListener {
            if(binding.topbnrViewPager.isFakeDragging || binding.bannerViewPager.isFakeDragging){
                binding.topbnrViewPager.endFakeDrag()
                binding.bannerViewPager.endFakeDrag()
            }

            jobTopBnr.cancel()
            jobAdBnr.cancel()

            getApiData()

            binding.topbnrViewPager.adapter?.notifyDataSetChanged()
            binding.bannerViewPager.adapter?.notifyDataSetChanged()
            binding.followingRecylcerView.adapter?.notifyDataSetChanged()
            topBnrAutoScroll()
            adBnrAutoScroll()

            binding.refresh.isRefreshing = false
        }
    }

    private fun mainViewModelFlow(){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    mainViewModel.topBnrLists.collect{topBnrLists->
                        binding.topbnrViewPager.apply {
                            setCurrentItem(Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % topBnrLists.size, false)
                            adapter = TopBnrAdapter(topBnrLists)

                            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                                override fun onPageScrollStateChanged(state: Int) {
                                    super.onPageScrollStateChanged(state)
                                    when (state) {
                                        ViewPager2.SCROLL_STATE_IDLE -> {
                                            if (!jobTopBnr.isActive) topBnrAutoScroll()
                                        }
                                        ViewPager2.SCROLL_STATE_DRAGGING -> { jobTopBnr.cancel() }
                                        ViewPager2.SCROLL_STATE_SETTLING -> {}
                                    }
                                }
                            })
                            setPageTransformer { page, position ->
//                    val itemTopbnrBinding = ItemTopbnrBinding.bind(page)
                                val binding = page.tag as? ItemTopbnrBinding // 어뎁터에서 만들어놓은 bind 객체 사용
                                binding?.topbnrTextLayout?.translationX = position * page.width / 2
                            }

                            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                                override fun onGlobalLayout() {
                                    actionbarWChangeBoundary = (binding.topbnrViewPager.height * (1/2.0)).toInt()
                                    binding.topbnrViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                                }
                            })

                        }
                    }
                }
                launch {
                    mainViewModel.followingList.collect{ followingList ->
                        if (followingList.isEmpty()){
                            binding.followingRecylcerView.visibility = View.GONE
                        } else {
                            binding.followingRecylcerView.apply {
                                adapter = FollowingAdapter(followingList)
                                layoutManager =
                                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//                    addItemDecoration(FollowingDecorator())
                            }
                        }
                    }
                }
                launch {
                    mainViewModel.eventLists.collect{ eventLists ->
                        binding.bannerViewPager.apply {
                            adapter = BannerAdapter(eventLists)
                            setCurrentItem(Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % eventLists.size, false)
                            binding.bannerText.text = getString(R.string.banner_count, 1, eventLists.size)
                            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                                override fun onPageSelected(position: Int) {
                                    super.onPageSelected(position)
                                    val realPosition = position % eventLists.size
                                    binding.bannerText.text =
                                        getString(R.string.banner_count, realPosition + 1, eventLists.size)
                                }

                                override fun onPageScrollStateChanged(state: Int) {
                                    super.onPageScrollStateChanged(state)
                                    when (state) {
                                        ViewPager2.SCROLL_STATE_IDLE -> {
                                            if (!jobAdBnr.isActive) adBnrAutoScroll()
                                        }
                                        ViewPager2.SCROLL_STATE_DRAGGING -> jobAdBnr.cancel()
                                        ViewPager2.SCROLL_STATE_SETTLING -> {}
                                    }
                                }
                            })
                        }
                    }
                }
                launch {
                    mainViewModel.liveSectionLists.collect{ liveSectionLists ->
                        binding.liveSectionRecylcerView.apply {
                            adapter = LiveSectionAdapter(liveSectionLists)
                            layoutManager =
                                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        }
                    }
                }
            }
        }
    }

    private fun setObserve() = with(mainViewModel){

        /*topBnrLists.observe(this@MainActivity){ topBnrLists ->
            binding.topbnrViewPager.apply {
                setCurrentItem(Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % topBnrLists.size, false)
                adapter = TopBnrAdapter(topBnrLists)

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        when (state) {
                            ViewPager2.SCROLL_STATE_IDLE -> {
                                if (!jobTopBnr.isActive) topBnrAutoScroll()
                            }
                            ViewPager2.SCROLL_STATE_DRAGGING -> { jobTopBnr.cancel() }
                            ViewPager2.SCROLL_STATE_SETTLING -> {}
                        }
                    }
                })
                setPageTransformer { page, position ->
//                    val itemTopbnrBinding = ItemTopbnrBinding.bind(page)
                    val binding = page.tag as? ItemTopbnrBinding // 어뎁터에서 만들어놓은 bind 객체 사용
                    binding?.topbnrTextLayout?.translationX = position * page.width / 2
                }

                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                        actionbarWChangeBoundary = (binding.topbnrViewPager.height * (1/2.0)).toInt()
                        binding.topbnrViewPager.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })

            }
        }*/
       /* followingList.observe(this@MainActivity){ followingList ->
            if (followingList.isEmpty()){
                binding.followingRecylcerView.visibility = View.GONE
            } else {
                binding.followingRecylcerView.apply {
                    adapter = FollowingAdapter(followingList)
                    layoutManager =
                        LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//                    addItemDecoration(FollowingDecorator())
                }
            }
        }*/
        /*eventLists.observe(this@MainActivity){ eventLists ->
            binding.bannerViewPager.apply {
                adapter = BannerAdapter(eventLists)
                setCurrentItem(Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % eventLists.size, false)
                binding.bannerText.text = getString(R.string.banner_count, 1, eventLists.size)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        val realPosition = position % eventLists.size
                        binding.bannerText.text =
                            getString(R.string.banner_count, realPosition + 1, eventLists.size)
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        when (state) {
                            ViewPager2.SCROLL_STATE_IDLE -> {
                                if (!jobAdBnr.isActive) adBnrAutoScroll()
                            }
                            ViewPager2.SCROLL_STATE_DRAGGING -> jobAdBnr.cancel()
                            ViewPager2.SCROLL_STATE_SETTLING -> {}
                        }
                    }
                })
            }
        }*/
        /*liveSectionLists.observe(this@MainActivity){ liveSectionLists ->
            binding.liveSectionRecylcerView.apply {
                adapter = LiveSectionAdapter(liveSectionLists)
                layoutManager =
                    LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            }
        }*/

        bjText.observe(this@MainActivity){
            // 나중에 클릭시 동작을 정의하기 위해
            if (it){
                binding.textBJ.setTextAppearance(R.style.suitBoldStyle2)
            } else {
                binding.textBJ.setTextAppearance(R.style.suitSemiBoldStyle2)
            }
        }
        fanText.observe(this@MainActivity){
            if (it){
                binding.textFan.setTextAppearance(R.style.suitBoldStyle2)
            } else {
                binding.textFan.setTextAppearance(R.style.suitSemiBoldStyle2)
            }
        }
        teamText.observe(this@MainActivity){
            if(it){
                binding.textTeam.setTextAppearance(R.style.suitBoldStyle2)
            } else {
                binding.textTeam.setTextAppearance(R.style.suitSemiBoldStyle2)
            }
        }
    }

    private fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen","android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    private fun getNavigationBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen","android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }


    fun topBnrAutoScroll(){
        jobTopBnr = lifecycleScope.launch{
            delay(3000)
            binding.topbnrViewPager.setCurrentItemWithDuration(binding.topbnrViewPager.currentItem + 1, 2000)
        }
    }
    fun adBnrAutoScroll(){
        jobAdBnr = lifecycleScope.launch{
            delay(3500)
            binding.bannerViewPager.setCurrentItemWithDuration(binding.bannerViewPager.currentItem + 1, 2000)
        }
    }

    override fun onPause() {
        super.onPause()
        jobTopBnr.cancel()
        jobAdBnr.cancel()
    }

    private val maxScrollY by lazy {
        binding.topbnrViewPager.height - binding.actionBarW.height
    }

    override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        if(scrollY > actionbarWChangeBoundary){
            val alpha = when {
                scrollY >= maxScrollY -> 1f
                else -> (scrollY - actionbarWChangeBoundary).toFloat() / (maxScrollY - actionbarWChangeBoundary)
            }
            binding.dallaLogo.visibility = View.VISIBLE
            binding.dallaLogo.alpha = alpha
            binding.actionBarW.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.white))
//            binding.actionBarW.setBackgroundResource(R.color.white)
            binding.actionBarW.background.alpha = (alpha * 255).roundToInt()

            if(alpha < 0.4){
                overChangeBoundary()
                binding.btnAlarmW.alpha = 1f
                binding.btnMessageW.alpha = 1f
                binding.btnRankingW.alpha = 1f
                binding.btnStoreW.alpha = 1f
            } else {
                underChangeBoundary()
                binding.btnAlarmW.alpha = alpha
                binding.btnMessageW.alpha = alpha
                binding.btnRankingW.alpha = alpha
                binding.btnStoreW.alpha = alpha
            }
        } else {
            binding.actionBarW.setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.transparent))
//            binding.actionBarW.setBackgroundResource(android.R.color.transparent)
            binding.dallaLogo.visibility = View.GONE
            overChangeBoundary()
            binding.actionBarW.alpha = 1f
        }
    }

    private fun underChangeBoundary() {
        binding.btnAlarmW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_alarm_b))
        binding.btnMessageW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_message_b))
        binding.btnRankingW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_ranking_b))
        binding.btnStoreW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_store_b))
    }

    private fun overChangeBoundary(){
        binding.btnAlarmW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_alarm_w))
        binding.btnMessageW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_message_w))
        binding.btnRankingW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_ranking_w))
        binding.btnStoreW.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.btn_store_w))
    }

    private fun ViewPager2.setCurrentItemWithDuration(
        item: Int, duration: Long,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
        pagePxWidth: Int = width
    ){
        val pxToDrag: Int = pagePxWidth * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0

        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }

        animator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) { beginFakeDrag() }
            override fun onAnimationEnd(animation: Animator) { endFakeDrag() }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }
}

