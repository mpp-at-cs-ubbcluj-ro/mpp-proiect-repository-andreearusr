
import React from  'react';
class ArtistForm extends React.Component{

    constructor(props) {
        super(props);
        this.state = {id: '', firstName:'', lastName:'', age:'', originCountry:''};

        //  this.handleChange = this.handleChange.bind(this);
        // this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleIdChange=(event) =>{
        this.setState({id: event.target.value});
    }

    handleFirstNameChange=(event) =>{
        this.setState({firstName: event.target.value});
    }

    handleLastNameChange=(event) =>{
        this.setState({lastName: event.target.value});
    }

    handleAgeChange=(event) =>{
        this.setState({age: event.target.value});
    }

    handleOriginCountryChange=(event) =>{
        this.setState({originCountry: event.target.value});
    }


    handleAdd =(event) =>{

        let artist={id:this.state.id,
            firstName:this.state.firstName,
            lastName:this.state.lastName,
            age:this.state.age,
            originCountry:this.state.originCountry
        }
        console.log('An artist was submitted: ');
        console.log(artist);
        this.props.addFunc(artist);
        event.preventDefault();
    }

    handleModify =(event) =>{

        let artist={id:this.state.id,
            firstName:this.state.firstName,
            lastName:this.state.lastName,
            age:this.state.age,
            originCountry:this.state.originCountry
        }
        console.log('An artist was updated: ');
        console.log(artist);
        this.props.modifyFunc(artist.id, artist);
        event.preventDefault();
    }

    render() {
        return (
            <div>
                <label>
                    Id:
                    <input type="number" value={this.state.id} onChange={this.handleIdChange} />
                </label><br/>
                <label>
                    FirstName:
                    <input type="text" value={this.state.firstName} onChange={this.handleFirstNameChange} />
                </label><br/>
                <label>
                    LastName:
                    <input type="text" value={this.state.lastName} onChange={this.handleLastNameChange} />
                </label><br/>
                <label>
                    Age:
                    <input type="number" value={this.state.age} onChange={this.handleAgeChange} />
                </label><br/> <label>
                    OriginCountry:
                    <input type="text" value={this.state.originCountry} onChange={this.handleOriginCountryChange} />
                </label><br/> <br/>

                <button onClick={this.handleAdd}>Add artist</button>
                <button onClick={this.handleModify}>Modify artist</button>

            </div>
        );
    }
}
export default ArtistForm;