package com.example.tareafinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CountryAdapter(
    private var countries: List<Country>,
    private val onClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return CountryViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    fun updateCountries(newCountries: List<Country>) {
        countries = newCountries
        notifyDataSetChanged() // Notifica al adaptador que los datos han cambiado
    }

    override fun getItemCount(): Int = countries.size

    class CountryViewHolder(itemView: View, val onClick: (Country) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val capitalTextView: TextView = itemView.findViewById(R.id.textViewCapital)
        private val regionTextView: TextView = itemView.findViewById(R.id.textViewRegion)
        private val flagImageView: ImageView = itemView.findViewById(R.id.imageViewFlag)
        private lateinit var country: Country

        init {
            itemView.setOnClickListener {
                onClick(country)
            }
        }

        fun bind(country: Country) {
            this.country = country
            nameTextView.text = country.name.common
            capitalTextView.text = country.capital?.joinToString() ?: "No Capital"
            regionTextView.text = country.region
            Picasso.get().load(country.flags.png).into(flagImageView)
        }
    }
}

