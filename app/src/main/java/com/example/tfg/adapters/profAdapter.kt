package com.example.tfg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tfg.R
import com.example.tfg.models.Model


class profAdapter (private val profList: ArrayList<Model>) : RecyclerView.Adapter<profAdapter.ViewHolder>() {

    private var mListener: onItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    override fun onBindViewHolder(holder: profAdapter.ViewHolder, position: Int) {
        val currentProf = profList[position]
        holder.tvProfName.text = currentProf.Nombre
        holder.tvProfApellido.text = currentProf.Apellido1
        holder.tvProfTitulo.text = currentProf.Titulo
        holder.tvProfGrado.text = currentProf.Grado
        holder.tvProfTlf.text = currentProf.Tlf
        holder.tvProfMail.text = currentProf.Email

        mListener?.let { listener ->
            holder.itemView.setOnClickListener {
                listener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return profList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        val tvProfName: TextView = itemView.findViewById(R.id.nombreProfesorRV)
        val tvProfApellido: TextView = itemView.findViewById(R.id.apellidoProfRV)
        val tvProfTitulo: TextView = itemView.findViewById(R.id.TituloProfRV)
        val tvProfGrado: TextView = itemView.findViewById(R.id.GradoProfRV)
        val tvProfTlf: TextView = itemView.findViewById(R.id.TlfProfRV)
        val tvProfMail: TextView = itemView.findViewById(R.id.MailProfRV)

        init {
            clickListener?.let { listener ->
                itemView.setOnClickListener {
                    listener.onItemClick(adapterPosition)
                }
            }
        }
    }
}
