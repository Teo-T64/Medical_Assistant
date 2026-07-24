import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField } from "@mui/material";
import type React from "react";
import { useState } from "react";
import type { Symptom } from "../util/types";
import { SYMPTOM_TYPE_LIST } from "../util/constants";

function SymptomForm({onSymptomAdded}){
    const [symptom,setSymptom] = useState<Symptom>({type:"OTHER",description:""});


    async function handleSubmit(e:React.SubmitEvent){
        e.preventDefault();
        try {
            await addSymptom(symptom);
            onSymptomAdded();
            setSymptom({type:"OTHER",description:""});
        } catch (error) {
            console.log(error);
        }
    }

    return(
        <div>
            <Box component="form" onSubmit={handleSubmit} sx={{ p: 2, border: '0.5px solid lightgrey', borderRadius:'10px', boxShadow:'0px 0px 1px lightgray' }}>
                <FormControl fullWidth sx={{mb: 2}}>
                    <InputLabel>Symptom Type</InputLabel>
                    <Select value={symptom.type} onChange={(e)=>setSymptom({...symptom,type:e.target.value})}>
                        {
                            SYMPTOM_TYPE_LIST.map((el)=>(
                                <MenuItem key={el.value} value={el.value}>{el.name}</MenuItem>
                            ))
                        }

                    </Select>
                </FormControl>
                <TextField 
                    fullWidth label='Description' 
                    type="text" 
                    sx={{mb:2}} 
                    value={symptom.description} 
                    onChange={(e)=>setSymptom({...symptom,description:e.target.value})}
                />
                <Button type="submit" variant="contained">Add Symptom</Button>
            </Box>
        </div>
    )
}

export default SymptomForm;