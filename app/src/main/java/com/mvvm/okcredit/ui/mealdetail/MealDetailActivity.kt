package com.mvvm.okcredit.ui.mealdetail

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvvm.okcredit.R
import com.mvvm.okcredit.databinding.MealDetailActivityBinding
import com.mvvm.okcredit.repository.RepoModel
import com.mvvm.okcredit.ui.mealdetail.model.MealDetailsModelResponse
import com.mvvm.okcredit.ui.viewmodel.ExploreFactory
import com.mvvm.okcredit.ui.viewmodel.MainActivityVM
import com.mvvm.okcredit.util.ProgressState
import org.koin.android.ext.android.inject

class MealDetailActivity : AppCompatActivity() {

    val repo: RepoModel by inject()
    lateinit var binding: MealDetailActivityBinding
    lateinit var viewmodel: MainActivityVM
    var getIdMeal = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_detail_activity)

        //Todo("Data Binding & ViewModel")
        binding = DataBindingUtil.setContentView<MealDetailActivityBinding>(
            this,
            R.layout.meal_detail_activity
        )
        viewmodel =
            ViewModelProviders.of(this, ExploreFactory(repo)).get(MainActivityVM(repo)::class.java)

        //Todo("Get Data from another activity using Intent")
        val extras = intent.extras
        if (extras != null) {
            getIdMeal = extras.getInt("idMeal", 0)
            Log.e("getIdMeal", getIdMeal.toString())
        }

        binding.lifecycleOwner = this
        binding.executePendingBindings()
        if (isInternetConnected())
            viewmodel.callMealDetailsListAPI(getIdMeal)//52893

        observerLiveData()

    }
    private fun observerLiveData() {
        viewmodel.progressState.observe(this, Observer {
            if (it.equals(ProgressState.SHOW)) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        })
        viewmodel.mealClassResponse.observe(this, Observer {
            if (it != null) {
                binding.viewmodel = viewmodel.mealDetailsItemArrayList.get(0)
                /* val  strInstructions=.get(0).strInstructions
                 .e("strInstructions",strInstructions)*/
                setData(it.meals)
            } else {
                //binding.tvNoData.visibility = View.VISIBLE,
                // binding.rvCategoryList.visibility = View.GONE
                //binding.rvProductList.visibility = View.GONE
            }
        })


    }

    private fun setData(mealsSubItems: List<MealDetailsModelResponse.Meal>) {
        Log.e("strInstructions", mealsSubItems.get(0).strInstructions)

    }

    //Todo (Internet Connection  )
    fun isInternetConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected && networkInfo.isConnectedOrConnecting
    }

    private lateinit var mDialog: Dialog
    private fun showProgressDialog() {
        mDialog = Dialog(this, R.style.MaterialDialogSheet)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setContentView(R.layout.progress_loader)
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    fun dismissProgressDialog() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }


}