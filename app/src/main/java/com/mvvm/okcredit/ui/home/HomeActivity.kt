package com.mvvm.okcredit.ui.home


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mvvm.okcredit.R
import com.mvvm.okcredit.databinding.ActivityHomeBinding
import com.mvvm.okcredit.repository.RepoModel
import com.mvvm.okcredit.ui.home.model.FoodClassResponse
import com.mvvm.okcredit.ui.mealdetail.MealDetailActivity
import com.mvvm.okcredit.ui.viewmodel.ExploreFactory
import com.mvvm.okcredit.ui.viewmodel.MainActivityVM
import com.mvvm.okcredit.util.ProgressState
import org.koin.android.ext.android.inject


class HomeActivity : AppCompatActivity() {


    val repo: RepoModel by inject()
    lateinit var binding: ActivityHomeBinding
    lateinit var viewmodel: MainActivityVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Todo("Data Binding & ViewModel")
        binding = DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
        viewmodel =
            ViewModelProviders.of(this, ExploreFactory(repo)).get(MainActivityVM(repo)::class.java)


        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        if (isInternetConnected())
            viewmodel.callProductListItemsAPI()
        observerLiveData()


        //Todo (Recyclerview searchView)
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showProgressDialog()
                filter(query.toString())
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        //Todo (Recyclerview Reload  data after Clear text)
        val closeBtn: View = binding.search.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeBtn.setOnClickListener {
            binding.search.setQuery("", false)
            binding.search.isIconified = true
            if (isInternetConnected())
                viewmodel.callProductListItemsAPI()
            observerLiveData()
        }


    }

    var posMeal = 0;
    private fun observerLiveData() {
        viewmodel.progressState.observe(this, Observer {
            if (it.equals(ProgressState.SHOW)) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        })

        viewmodel.selectedItemPositionMealDetails.observe(this, Observer {
            for (i in viewmodel.foodClassArrayList.indices) {
                if (i == it) {
                    posMeal = i
                    viewmodel.foodClassArrayList[it].isSelected = true
                } else {
                    viewmodel.foodClassArrayList[i].isSelected = false
                }
            }


            //Todo(Move One activity to another Activity)
            val intent = Intent(baseContext, MealDetailActivity::class.java)
            intent.putExtra(
                "idMeal", binding.viewmodel!!.foodClassArrayList.get(posMeal).idMeal.toInt()
            )
            startActivity(intent)

            showToast(this,binding.viewmodel!!.foodClassArrayList.get(posMeal).strMeal)

        })

        viewmodel.foodClassResponse.observe(this, Observer {
            if (it.meals.size > 0) {
                val strMealName = it.meals.get(0).strMeal
                Log.e("strMealName", strMealName)
            //binding.rvCategoryList.visibility = View.VISIBLE
            } else {
                //binding.tvNoData.visibility = View.VISIBLE,
                // binding.rvCategoryList.visibility = View.GONE
                //binding.rvProductList.visibility = View.GONE
            }
        })
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

    private fun filter(text: String) {
        val filteredlist: ArrayList<FoodClassResponse.Meal> = ArrayList()
        for (item in viewmodel.foodClassArrayList) {
            if (item.idMeal.toLowerCase().contains(text.toLowerCase()) || item.strMeal.toLowerCase()
                    .contains(text.toLowerCase())
            ) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            showToast(this,"No Data Found..")
            dismissProgressDialog()
        } else {
            viewmodel.foolListAdapter.filterList(filteredlist)
            dismissProgressDialog()

        }
    }

    fun showToast(context: Context?, info: String) {
        val toast = Toast.makeText(
            context,
            Html.fromHtml("<font color='#FF0000' ><b>$info</b></font>"),
            Toast.LENGTH_LONG
        )
        toast.setGravity(Gravity.TOP, 0, 0)
        toast.show()
    }


}