package com.terabyte.digitallyceum.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.terabyte.digitallyceum.Const
import com.terabyte.digitallyceum.R
import com.terabyte.digitallyceum.databinding.ActivityMainMenuBinding
import com.terabyte.digitallyceum.fragment.EventsFragment
import com.terabyte.digitallyceum.fragment.MainFragment
import com.terabyte.digitallyceum.fragment.ScheduleFragment
import com.terabyte.digitallyceum.fragment.TeachersFragment
import com.terabyte.digitallyceum.json.grades.GradeJson
import com.terabyte.digitallyceum.json.schools.SchoolJson
import com.terabyte.digitallyceum.json.subgroups.SubgroupJson
import com.terabyte.digitallyceum.viewmodel.MainMenuViewModel
import com.terabyte.digitallyceum.viewmodel.NoResponseViewModel

class MainMenuActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
  private lateinit var school: SchoolJson
  private lateinit var grade: GradeJson
  private lateinit var subgroup: SubgroupJson
  private lateinit var viewModel: MainMenuViewModel

  private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        school = intent?.extras!!.getParcelable<SchoolJson>(Const.INTENT_SCHOOL)!!
        grade = intent?.extras!!.getParcelable<GradeJson>(Const.INTENT_GRADE)!!
        subgroup = intent?.extras!!.getParcelable<SubgroupJson>(Const.INTENT_SUBGROUP)!!

        viewModel = ViewModelProvider(this, MainMenuViewModel.Factory(application, school, grade, subgroup))[MainMenuViewModel::class.java]

        viewModel.liveDataSchedule.observe(this){ schedule ->
            if(schedule==null || schedule.isEmpty()) {
                val intent = Intent(this, NoResponseActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    .putExtra(Const.INTENT_NO_RESPONSE_TYPE, NoResponseViewModel.NoResponseType.Lessons)
                    .putExtra(Const.INTENT_SCHOOL, school)
                    .putExtra(Const.INTENT_GRADE, grade)
                    .putExtra(Const.INTENT_SUBGROUP, subgroup)
                startActivity(intent)
            }
            else {
                val binding = ActivityMainMenuBinding.inflate(layoutInflater)
                setContentView(binding.root)

                drawerLayout = binding.drawerLayoutMainMenu

                val toolbar = findViewById<Toolbar>(R.id.toolbarMainMenu)
                setSupportActionBar(toolbar)

                val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close)
                toggle.syncState()

                binding.navViewMainMenu.setNavigationItemSelectedListener(this)
                // TODO: here we hid actionbar, but we need to change the title
                viewModel.liveDataChosenNavViewItemId.observe(this){ id ->
                    binding.navViewMainMenu.setCheckedItem(id)
                    val fragment = when(id) {
                        R.id.menuItemMain -> {
                            toolbar.title = resources.getString(R.string.app_name)
                            MainFragment()
                        }
                        R.id.menuItemSchedule -> {
                            toolbar.title = resources.getString(R.string.navLabelSchedule)
                            ScheduleFragment()
                        }
                        R.id.menuItemTeachers -> {
                            toolbar.title = resources.getString(R.string.navLabelTeachers)
                            TeachersFragment()
                        }
                        R.id.menuItemEvents -> {
                            toolbar.title = resources.getString(R.string.navLabelEvents)
                            EventsFragment()
                        }
                        else -> {
                            Log.e(Const.LOG_TAG_DRAWER_INCORRECT_MENU_ITEM_ID, "The id in onNavigationItemSelected is incorrect!")
                            MainFragment()
                        }
                    }
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMainMenu, fragment)
                        .commit()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Const.INTENT_SCHOOL, school)
        outState.putParcelable(Const.INTENT_GRADE, grade)
        outState.putParcelable(Const.INTENT_SUBGROUP, subgroup)
        super.onSaveInstanceState(outState)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment = when(item.itemId) {
            R.id.menuItemMain -> MainFragment()
            R.id.menuItemSchedule -> ScheduleFragment()
            R.id.menuItemTeachers -> TeachersFragment()
            R.id.menuItemEvents -> EventsFragment()
            else -> {
                Log.e(Const.LOG_TAG_DRAWER_INCORRECT_MENU_ITEM_ID, "The id in onNavigationItemSelected is incorrect!")
                MainFragment()
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.frameLayoutMainMenu, fragment)
            .commit()

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}